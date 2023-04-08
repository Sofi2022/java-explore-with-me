package ru.practicum.server.service;

import ru.practicum.stat.EndpointHitDto;
import ru.practicum.stat.ViewStateDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatService {

    EndpointHitDto createEndpoint(EndpointHitDto endpointHitDto);

    List<ViewStateDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
