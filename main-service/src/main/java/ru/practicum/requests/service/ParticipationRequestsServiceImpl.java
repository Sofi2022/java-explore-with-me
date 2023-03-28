package ru.practicum.requests.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.events.model.Event;
import ru.practicum.events.repository.EventsRepository;
import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.requests.mapper.ParticipationRequestsMapper;
import ru.practicum.requests.model.ParticipationRequest;
import ru.practicum.requests.repository.ParticipationRequestsRepository;
import ru.practicum.state.State;
import ru.practicum.user.exception.NotFoundException;
import ru.practicum.user.exception.RequestAlreadyExists;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ParticipationRequestsServiceImpl implements RequestService {

    private final UserRepository userRepository;

    private final EventsRepository eventsRepository;

    private final ParticipationRequestsRepository participationRequestsRepository;

    private final ParticipationRequestsMapper partMapper;

    @Override
    public ParticipationRequestDto addRequest(Long userId, Long eventId) {
        User requester = userRepository.findById(userId).orElseThrow(() -> new NotFoundException( "Такого пользователя нет "
        + userId));
        Event event = eventsRepository.findById(eventId).orElseThrow(() -> new NotFoundException( "Такого события нет "
                + userId));
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
            if(!event.getRequestModeration()) {
                request.setState(State.CONFIRMED);
            } else {
                request.setState(State.PENDING);
            }
            ParticipationRequest savedRequest = participationRequestsRepository.save(request);
            //Меняем кол-во подтвержденных заявок у события
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            return partMapper.toDto(savedRequest);
        } else {
            throw new RequestAlreadyExists("Нельзя добавить повторный запрос: userId {}, eventId {} " + userId + eventId);
        }
    }
}
