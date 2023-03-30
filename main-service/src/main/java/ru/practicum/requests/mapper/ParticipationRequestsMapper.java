package ru.practicum.requests.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.requests.dto.EventRequestStatusUpdateResult;
import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.requests.model.ParticipationRequest;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ParticipationRequestsMapper {

    @Mapping(target = "event", source = "event.id")
    @Mapping(target = "requester", source = "request.id")
    @Mapping(target = "status", source = "request.state")
    ParticipationRequestDto toDto(ParticipationRequest request);

    List<ParticipationRequestDto> toListDto(List<ParticipationRequest> requests);

    EventRequestStatusUpdateResult toRequestStatusDto(List<ParticipationRequestDto> confirmedRequests,
                                                      List<ParticipationRequestDto> rejectedRequests);
}
