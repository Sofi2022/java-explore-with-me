package ru.practicum.server.service;

import ru.practicum.EndpointHitDto;
import ru.practicum.ViewStateDto;

import java.util.List;

public interface StatService {

    EndpointHitDto createEndpoint(EndpointHitDto endpointHitDto);

    List<ViewStateDto> getStats(String start, String end, List<String> uris, boolean unique);
}
