package ru.practicum.requests.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.events.model.Event;
import ru.practicum.events.repository.EventsRepository;
import ru.practicum.requests.EventRequestStatusUpdateRequest;
import ru.practicum.requests.EventRequestStatusUpdateResult;
import ru.practicum.requests.ParticipationRequestDto;
import ru.practicum.requests.mapper.ParticipationRequestsMapper;
import ru.practicum.requests.model.ParticipationRequest;
import ru.practicum.requests.repository.ParticipationRequestsRepository;
import ru.practicum.state.State;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.RequestAlreadyExists;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParticipationRequestsServiceImpl implements RequestService {

    private final UserRepository userRepository;

    private final EventsRepository eventsRepository;

    private final ParticipationRequestsRepository participationRequestsRepository;

    private final ParticipationRequestsMapper partMapper;


    @Transactional
    @Override
    public ParticipationRequestDto addRequest(Long userId, Long eventId) {
        User requester = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Такого пользователя нет "
        + userId));
        Event event = eventsRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Такого события нет "
                + eventId));
        ParticipationRequest request = new ParticipationRequest(LocalDateTime.now(), event, requester, State.PENDING);
        List<ParticipationRequest> requests = participationRequestsRepository.findAllByRequesterIdAndEventId(userId, eventId);
        if (event.getInitiator().getId() == userId) {
            throw new RequestAlreadyExists("Инициатор события не может добавить запрос на участие в своём событии " + userId);
        }
        if (!(event.getState().equals(State.PUBLISHED))) {
            throw new RequestAlreadyExists("Нельзя участвовать в неопубликованном событии");
        }
        int limit = event.getParticipantLimit();
        if (limit != 0) {
            if (limit == event.getConfirmedRequests()) {
                throw new RequestAlreadyExists("У события достигнут лимит запросов на участие: " + limit);
            }
        }

        if (requests.isEmpty()) {
            if (!event.getRequestModeration()) {
                request.setState(State.CONFIRMED);
                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            } else {
                request.setState(State.PENDING);
            }
            ParticipationRequest savedRequest = participationRequestsRepository.save(request);
            return partMapper.toDto(savedRequest);
        } else {
            throw new RequestAlreadyExists("Нельзя добавить повторный запрос: userId {}, eventId {} " + userId + eventId);
        }
    }


    @Override
    public List<ParticipationRequestDto> getUserRequestsInForeignEvents(Long userId) {
        validateUser(userId);
        List<ParticipationRequest> requests = participationRequestsRepository.findAllByRequesterIdInForeignEvents(userId);
        return partMapper.toListDto(requests);
    }


    @Override
    public List<ParticipationRequestDto> getUserRequestsInEvent(Long userId, Long eventId) {
        validateUser(userId);
        validateEvent(eventId);
        List<ParticipationRequest> requests = participationRequestsRepository.findAllUserRequestsInEvent(userId, eventId);
        return partMapper.toListDto(requests);
    }


    @Transactional
    @Override
    public ParticipationRequestDto cancelRequestByUser(Long userId, Long requestId) {
        validateUser(userId);
        ParticipationRequest request = participationRequestsRepository.findById(requestId).orElseThrow(() ->
                new NotFoundException("Такой заявки нет " + requestId));
        request.setState(State.CANCELED);
        return partMapper.toDto(participationRequestsRepository.save(request));
    }


    @Transactional
    @Override
    public EventRequestStatusUpdateResult confirmOrRejectRequestsByUser(Long userId, Long eventId,
                                                                        EventRequestStatusUpdateRequest request) {
        validateUser(userId);
        Event event = eventsRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Такого события нет "
                + eventId));
        if (event.getParticipantLimit() == 0 && !event.getRequestModeration()) {
            throw new RequestAlreadyExists("Подтверждение заявки не требуется " + eventId);
        }
        if (event.getConfirmedRequests() >= event.getParticipantLimit()) {
            throw new RequestAlreadyExists("Превышен лимит подтвержденных заявок " + eventId);
        }

        List<Long> requestIds = request.getRequestIds();

        String status = request.getStatus();

        List<ParticipationRequest> requests = requestIds.stream().map((id) -> participationRequestsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Такой заявки нет "
                + id))).collect(Collectors.toList());

        List<ParticipationRequest> confirmedRequests = new ArrayList<>();
        List<ParticipationRequest> rejectedRequests = new ArrayList<>();


        for (ParticipationRequest req : requests) {
                if (status.equals("CONFIRMED") && req.getState().equals(State.PENDING)) {
                    if (event.getConfirmedRequests() >= event.getParticipantLimit()) {
                        req.setState(State.REJECTED);
                        participationRequestsRepository.save(req);
                        rejectedRequests.add(req);
                    }
                    req.setState(State.CONFIRMED);
                    participationRequestsRepository.save(req);
                    event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                    eventsRepository.save(event);
                    confirmedRequests.add(req);
                }
                if (status.equals("REJECTED") && req.getState().equals(State.PENDING)) {
                    req.setState(State.REJECTED);
                    participationRequestsRepository.save(req);
                    rejectedRequests.add(req);
                }
            }

        List<ParticipationRequestDto> con = partMapper.toListDto(confirmedRequests);
        List<ParticipationRequestDto> rej = partMapper.toListDto(rejectedRequests);

        return partMapper.toRequestStatusDto(con, rej);
    }

    private void validateUser(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Такого пользователя нет "
                + userId));
    }


    private void validateEvent(Long eventId) {
        eventsRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Такого события нет "
                + eventId));
    }
}
