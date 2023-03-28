package ru.practicum.events.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.mapper.CategoriesMapper;
import ru.practicum.categories.model.Category;
import ru.practicum.categories.repository.CategoriesRepository;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.dto.UpdateEventAdminRequest;
import ru.practicum.events.model.Event;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.NewEventDto;
import ru.practicum.events.mapper.EventsMapper;
import ru.practicum.events.repository.EventsRepository;
import ru.practicum.locations.dto.LocationDto;
import ru.practicum.locations.mapper.LocationMapper;
import ru.practicum.locations.model.Location;
import ru.practicum.locations.LocationRepository;
import ru.practicum.state.State;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.user.exception.NotFoundException;
import ru.practicum.user.exception.RequestAlreadyExists;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
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


    @Override
    public EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdminRequest event) {
        EventFullDto result;
        Event eventFromDb = eventsRepository.findById(eventId).orElseThrow(() -> new NotFoundException(
                "Такого события нет " + eventId));
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

        Event event1 = eventsMapper.updateEvent(event, eventFromDb);
        Event event2 = eventsRepository.save(eventFromDb);
        result = eventsMapper.toDto(eventFromDb);
        System.out.println("RESULT: " + result);
        return result;
    }


    @Override
    public List<EventFullDto> searchEvents(List<Long> userIds, List<String> states, List<Long> categories,
                                           String rangeStart, String rangeEnd, Integer from, Integer size) {
        List<EventFullDto> searchedEvents = new ArrayList<>();
        //if (from != 0) {
            int page = from / size;
            final PageRequest pageRequest = PageRequest.of(page, size);
            List<State> stateList = states.stream().map(State::valueOf).collect(Collectors.toList());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            LocalDateTime start = LocalDateTime.parse(rangeStart, formatter);
            LocalDateTime end = LocalDateTime.parse(rangeEnd, formatter);

            Page<Event> eventsWithPage = null;

            if (userIds.size() != 0 && states.size() != 0 && categories.size() != 0) {
                eventsWithPage = eventsRepository.findAllWithAllParameters(userIds, stateList, categories, start, end,
                        pageRequest);
            }
            if (userIds.size() == 0 && categories.size() != 0) {
                eventsWithPage = eventsRepository.findAllEventsWithoutIdList(categories, stateList, start, end, pageRequest);
            }

            List<Event> resultFromPage = eventsWithPage.getContent();
            if (resultFromPage.isEmpty()) {
                return searchedEvents;
            } else {
                return eventsMapper.toListFullDto(resultFromPage);
            }
    }


    @Override
    public List<EventFullDto> getEventsByUser(Long userId, Long eventId) {
        validateUser(userId);
        validateEvent(eventId);
        List<Event> events = eventsRepository.findAllByUser(userId, eventId);
        return eventsMapper.toListFullDto(events);
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
                                                 String rangeEnd, Boolean onlyAvailable, String sort, Integer from,
                                                 Integer size) {
        int page = from / size;
        final PageRequest pageRequest = PageRequest.of(page, size);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime start;
        LocalDateTime end;
        if (rangeStart.equals("0") && rangeEnd.equals("0")) {
            start = LocalDateTime.now();
            end = LocalDateTime.now();
        } else {
            start = LocalDateTime.parse(rangeStart, formatter);
            end = LocalDateTime.parse(rangeEnd, formatter);
        }

        Page<Event> events = null;
        List<Event> eventsFromRepo;

        if (categoriesIds.size() == 0 && sort.equals("EVENT_DATE")) {
            events = eventsRepository.findAllEventsFilteredWithPageWithoutCategoryEventDateAsc(pageRequest, text, start, end);
        }
        if (categoriesIds.size() == 0 && sort.equals("VIEWS")) {
            events = eventsRepository.findAllEventsFilteredWithPageWithoutCategoryViewsAsc(pageRequest, text, start, end);
        }
        if (categoriesIds.size() != 0 && sort.equals("EVENT_DATE")) {
            events = eventsRepository.findAllEventsFilteredWithCategoriesAndDateTimeAsc(pageRequest, text,
                    categoriesIds, start, end);
        }
        if (categoriesIds.size() != 0 && sort.equals("VIEWS")) {
            events = eventsRepository.findAllEventsFilteredWithCategoriesAndViewsAsc(pageRequest, text, categoriesIds, start, end);
        }

        eventsFromRepo = events.getContent();
        List<Event> result;


        if (paid) {
            result = eventsFromRepo.stream().filter(Event::getPaid).collect(Collectors.toList());
        } else {
            result = eventsFromRepo.stream().filter((event -> !event.getPaid())).collect(Collectors.toList());
        }

        if(onlyAvailable) {
            result = eventsFromRepo.stream().filter((event -> event.getConfirmedRequests() < event.getParticipantLimit()))
                    .collect(Collectors.toList());
        }
        return eventsMapper.toListShortDto(result);
    }

    private void validateUser(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Такого пользователя нет " + userId));
    }


    private void validateEvent(Long eventId) {
        eventsRepository.findById(eventId).orElseThrow(() -> new NotFoundException(
                "Такого события нет " + eventId));
    }
}
