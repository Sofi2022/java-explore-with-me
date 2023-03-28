package ru.practicum.requests.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.requests.model.ParticipationRequest;

@Mapper(componentModel = "spring")
public interface ParticipationRequestsMapper {

    @Mapping(target = "event", source = "event.id")
    @Mapping(target = "requester", source = "request.id")
    @Mapping(target = "status", source = "request.state")
    ParticipationRequestDto toDto(ParticipationRequest request);
}
