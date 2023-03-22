package ru.practicum.server.mapper;

import org.mapstruct.Mapper;
import ru.practicum.EndpointHitDto;
import ru.practicum.server.model.EndpointHit;

@Mapper(componentModel = "spring")
public interface StatMapper {
    EndpointHit fromDto(EndpointHitDto endpointHitDto);

    EndpointHitDto toDto(EndpointHit endpointHit);
}
