package ru.practicum.events.service;

import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.dto.NewEventDto;
import ru.practicum.events.dto.UpdateEventAdminRequest;

import java.util.List;

public interface EventService {

    EventFullDto createEvent(Long userId, NewEventDto event);

    EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdminRequest event);

    List<EventFullDto> searchEvents(List<Long> userIds, List<String> states, List<Long> categories, String rangeStart,
                                    String rangeEnd, Integer from, Integer size);
    List<EventFullDto> getEventsByUser(Long userId, Long eventId);

    List<EventShortDto> getEventsByUserWithPage(Long userId, Integer from, Integer size);

    List<EventShortDto> getEventsFiltered(String text, List<Long> categoriesIds, Boolean paid, String rangeStart,
                                          String rangeEnd, Boolean onlyAvailable, String sort, Integer from, Integer size);
}
