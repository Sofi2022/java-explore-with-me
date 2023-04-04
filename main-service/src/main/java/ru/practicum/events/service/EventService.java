package ru.practicum.events.service;

import ru.practicum.events.dto.*;
import ru.practicum.events.model.Event;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventService {

    EventFullDto createEvent(Long userId, NewEventDto event);

    EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdminRequest event);

    List<EventFullDto> searchEvents(List<Long> userIds, List<String> states, List<Long> categories, String rangeStart,
                                    String rangeEnd, Integer from, Integer size);
    EventFullDto getEventByUser(Long userId, Long eventId);

    List<EventShortDto> getEventsByUserWithPage(Long userId, Integer from, Integer size);

    List<EventShortDto> getEventsFiltered(String text, List<Long> categoriesIds, Boolean paid, String rangeStart,
                                          String rangeEnd, Boolean onlyAvailable, String sort, Integer from, Integer size,
                                          HttpServletRequest request);

    EventFullDto getFullEventById(Long eventId);

    EventFullDto updateEventByUser(Long userId, Long eventId, UpdateEventUserRequest event);

    Event getEventById(Long eventId);
}
