package ru.practicum.events.service;

import ru.practicum.events.dto.Event;
import ru.practicum.events.dto.NewEventDto;

public interface EventService {

    Event createEvent(Long userId, NewEventDto event);
}
