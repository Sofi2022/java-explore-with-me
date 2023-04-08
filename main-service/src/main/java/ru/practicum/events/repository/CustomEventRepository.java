package ru.practicum.events.repository;

import ru.practicum.events.model.Event;
import ru.practicum.events.model.EventSort;

import java.time.LocalDateTime;
import java.util.List;

public interface CustomEventRepository {

    List<Event> searchByParamsPublic(
            String text, List<Long> categories, Boolean paid,
            LocalDateTime rangeStart, LocalDateTime rangeEnd,
            Boolean onlyAvailable,
            EventSort sort, Integer from, Integer size
    );
}
