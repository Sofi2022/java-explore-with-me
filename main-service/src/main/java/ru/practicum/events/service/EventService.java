package ru.practicum.events.service;

import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.NewEventDto;
import ru.practicum.events.dto.UpdateEventAdminRequest;

public interface EventService {

    EventFullDto createEvent(Long userId, NewEventDto event);

    EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdminRequest event);
}
