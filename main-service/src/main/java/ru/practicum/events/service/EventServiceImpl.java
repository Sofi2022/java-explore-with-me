package ru.practicum.events.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.StateClient;
import ru.practicum.events.*;
import ru.practicum.events.model.EventSort;
import ru.practicum.categories.CategoryDto;
import ru.practicum.categories.mapper.CategoriesMapper;
import ru.practicum.categories.model.Category;
import ru.practicum.categories.repository.CategoriesRepository;
import ru.practicum.events.mapper.EventsUpdateMapper;
import ru.practicum.events.model.Event;
import ru.practicum.events.mapper.EventsMapper;
import ru.practicum.events.repository.EventsRepository;
import ru.practicum.locations.LocationDto;
import ru.practicum.locations.mapper.LocationMapper;
import ru.practicum.locations.model.Location;
import ru.practicum.locations.LocationRepository;
import ru.practicum.state.State;
import ru.practicum.user.UserShortDto;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.RequestAlreadyExists;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static ru.practicum.common.CommonConstant.TIME_PATTERN;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final UserRepository userRepository;

    private final EventsRepository eventsRepository;

    private final EventsMapper eventsMapper;

    private final LocationRepository locationRepository;

    private final LocationMapper locationMapper;

    private final CategoriesRepository categoriesRepository;

    private final UserMapper userMapper;

    private final CategoriesMapper categoriesMapper;

    private final EventsUpdateMapper eventsUpdateMapper;

    private final StateClient stateClient;


    @Transactional
    @Override
    public EventFullDto createEvent(Long userId, NewEventDto event) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Такого пользователя нет " + userId));
        validateTime(event.getEventDate());
        LocationDto locationDto = event.getLocation();
        Location location = locationMapper.fromDto(locationDto);
        locationRepository.save(location);
        Event eventToSave = eventsMapper.fromDto(event, userId);
        eventToSave.setState(State.PENDING);
        eventToSave.setConfirmedRequests(0);
        eventToSave.setCreatedOn(LocalDateTime.now());
        Event saved = eventsRepository.save(eventToSave);
        // Маппинг в FullEventDto
        Category category = categoriesRepository.findById(event.getCategory()).orElseThrow(() -> new NotFoundException(
                "Такой категории нет " + event.getCategory()));
        UserShortDto initiator = userMapper.toShortUser(user);
        EventFullDto fullEvent = eventsMapper.toDto(saved);
        fullEvent.setCreatedOn(LocalDateTime.now());
        CategoryDto cat = categoriesMapper.toCatDto(category);
        fullEvent.setCategory(cat);
        fullEvent.setInitiator(initiator);
        return fullEvent;
    }

    private void validateTime(LocalDateTime start) {
        if (start.isBefore(LocalDateTime.now())) {
            throw new RequestAlreadyExists("Дата и время события не может быть раньше, чем через 2 часа от " +
                    LocalDateTime.now());
        }
    }


    @Transactional
    @Override
    public EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdminRequest event) {
        Event eventFromDb = eventsRepository.findById(eventId).orElseThrow(() -> new NotFoundException(
                "Такого события нет " + eventId));
        if (event.getEventDate() != null) {
            validateTime(event.getEventDate());
        }
        if (event.getStateAction() != null) {
            if (event.getStateAction().equals("PUBLISH_EVENT")) {
                if (eventFromDb.getState().equals(State.PENDING)) {
                    eventFromDb.setState(State.PUBLISHED);
                    eventFromDb.setPublishedOn(LocalDateTime.now());
                } else {
                    throw new RequestAlreadyExists("Событие можно публиковать, только если оно в состоянии ожидания публикации" +
                            event.getStateAction());
                }
            }
            if (event.getStateAction().equals("REJECT_EVENT")) {
                if (eventFromDb.getState().equals(State.PUBLISHED)) {
                    throw new RequestAlreadyExists("Событие можно отклонить, только если оно еще не опубликовано" +
                            event.getStateAction());
                }
                eventFromDb.setState(State.CANCELED);
            }
        }

        eventsUpdateMapper.updateEventByAdmin(event, eventFromDb);
        eventsRepository.save(eventFromDb);
        return eventsMapper.toDto(eventFromDb);
    }


    @Override
    public List<EventFullDto> searchEvents(List<Long> userIds, List<String> states, List<Long> categories,
                                           String rangeStart, String rangeEnd, Integer from, Integer size, HttpServletRequest
                                                   request) {
        int page = from / size;
        final PageRequest pageRequest = PageRequest.of(page, size);
        if (states == null & rangeStart == null & rangeEnd == null) {
            return eventsMapper.toListFullDto(eventsRepository.findAll());
        }

        List<State> stateList = states.stream().map(State::valueOf).collect(Collectors.toList());

        LocalDateTime start;
        if (!(rangeStart.isEmpty())) {
            start = LocalDateTime.parse(rangeStart, DateTimeFormatter.ofPattern(TIME_PATTERN));
        } else {
            start = LocalDateTime.now().plusYears(5);
        }

        LocalDateTime end;
        if (!(rangeEnd.isEmpty())) {
            end = LocalDateTime.parse(rangeEnd, DateTimeFormatter.ofPattern(TIME_PATTERN));
        } else {
            end = LocalDateTime.now().plusYears(5);
        }

        if (userIds.size() != 0 && states.size() != 0 && categories.size() != 0) {
            Page<Event> eventsWithPage = eventsRepository.findAllWithAllParameters(userIds, stateList, categories, start, end,
                    pageRequest);
            return eventsMapper.toListFullDto(eventsWithPage.getContent());
        }
        if (userIds.size() == 0 && categories.size() != 0) {
            Page<Event> eventsWithPage = eventsRepository.findAllEventsWithoutIdList(categories, stateList, start, end, pageRequest);
            return eventsMapper.toListFullDto(eventsWithPage.getContent());
        } else {
            return new ArrayList<>();
        }
    }


    @Override
    public EventFullDto getEventByUser(Long userId, Long eventId) {
        validateUser(userId);
        Event event = eventsRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Такого пользователя нет " + userId));
        return eventsMapper.toDto(event);
    }


    @Override
    public List<EventShortDto> getEventsByUserWithPage(Long userId, Integer from, Integer size) {
        validateUser(userId);
        int page = from / size;
        final PageRequest pageRequest = PageRequest.of(page, size);
        Page<Event> eventsWithPage = eventsRepository.findAllByUserWithPage(userId, pageRequest);
        List<Event> events = eventsWithPage.getContent();
        return eventsMapper.toListShortDto(events);
    }


    @Override
    public List<EventShortDto> getEventsFiltered(String text, List<Long> categoriesIds, Boolean paid, String rangeStart,
                                                 String rangeEnd, Boolean onlyAvailable, EventSort sorted, Integer from,
                                                 Integer size, HttpServletRequest request) {
        log.info("PARAMS: " + text, categoriesIds, paid, rangeStart, rangeEnd, onlyAvailable, sorted, from, size);

        LocalDateTime start = null;
        LocalDateTime end;
        if (rangeStart == null) {
            start = LocalDateTime.now();
        }
        if (rangeEnd == null) {
            end = LocalDateTime.now();
        } else {
            start = LocalDateTime.parse(rangeStart, DateTimeFormatter.ofPattern(TIME_PATTERN));
            end = LocalDateTime.parse(rangeEnd, DateTimeFormatter.ofPattern(TIME_PATTERN));
        }


        List<Event> result = eventsRepository.searchByParamsPublic(text, categoriesIds, paid, start, end,
                onlyAvailable, sorted, from, size);

        if (result.size() == 0) {
            return Collections.emptyList();
        }


        Set<Long> eventIds = result.stream().map(Event::getId).collect(Collectors.toSet());
        Map<Long, Long> viewStatsMap = stateClient.getSetViewsByEventId(eventIds);

        List<EventShortDto> eventShort = eventsMapper.toListShortDto(result);

        eventShort.forEach(eventFullDto ->
                eventFullDto.setViews(viewStatsMap.getOrDefault(eventFullDto.getId(), 0L)));

        return eventShort;
    }


    @Override
    public EventFullDto getFullEventById(Long eventId, HttpServletRequest request) {
        Optional<Event> event = eventsRepository.findEventByIdAndStatePublished(eventId);
        if (event.isEmpty()) {
            throw new NotFoundException("Такого события нет " + eventId);
        } else {
            Long views = stateClient.getViewsByEventId(eventId);
            EventFullDto result = eventsMapper.toDto(event.get());
            result.setViews(views);
            return result;
        }
    }


    @Transactional
    @Override
    public EventFullDto updateEventByUser(Long userId, Long eventId, UpdateEventUserRequest event) {
        Event eventFromDb = eventsRepository.findById(eventId).orElseThrow(() -> new NotFoundException(
                "Такого события нет " + eventId));
        if (eventFromDb.getState().equals(State.CANCELED) || eventFromDb.getState().equals(State.PENDING)) {
            if (event.getEventDate() != null && event.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
                throw new RequestAlreadyExists("Дата и время на которые намечено событие не может быть раньше, " +
                        "чем через два часа от текущего момента ");
            }
            if (event.getStateAction().equals("SEND_TO_REVIEW")) {
                eventFromDb.setState(State.PENDING);
            }
            if (event.getStateAction().equals("CANCEL_REVIEW")) {
                eventFromDb.setState(State.CANCELED);
            }
        } else {
            throw new RequestAlreadyExists("Изменить можно только отмененные события или события в состоянии ожидания модерации, " +
                    "статус события = " + eventFromDb.getState());
        }

        eventsUpdateMapper.updateEventByUser(event, eventFromDb);
        eventsRepository.save(eventFromDb);
        return eventsMapper.toDto(eventFromDb);
    }


    @Override
    public Event getEventById(Long eventId) {
        return eventsRepository.findById(eventId).orElseThrow(() -> new NotFoundException(
                "Такого события нет " + eventId));
    }

    private void validateUser(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Такого пользователя нет " + userId));
    }
}
