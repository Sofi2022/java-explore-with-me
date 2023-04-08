package ru.practicum.requests.service;

import ru.practicum.requests.EventRequestStatusUpdateRequest;
import ru.practicum.requests.EventRequestStatusUpdateResult;
import ru.practicum.requests.ParticipationRequestDto;

import java.util.List;

public interface RequestService {

    ParticipationRequestDto addRequest(Long userId, Long eventId);

    List<ParticipationRequestDto> getUserRequestsInForeignEvents(Long userId);

    List<ParticipationRequestDto> getUserRequestsInEvent(Long userId, Long eventId);

    ParticipationRequestDto cancelRequestByUser(Long userId, Long requestId);

    EventRequestStatusUpdateResult confirmOrRejectRequestsByUser(Long userId, Long eventId, EventRequestStatusUpdateRequest
            request);
}
