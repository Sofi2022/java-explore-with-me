package ru.practicum.requests.service;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.requests.dto.EventRequestStatusUpdateRequest;
import ru.practicum.requests.dto.EventRequestStatusUpdateResult;
import ru.practicum.requests.dto.ParticipationRequestDto;

import javax.validation.Valid;
import java.util.List;

public interface RequestService {

    ParticipationRequestDto addRequest(Long userId, Long eventId);

    List<ParticipationRequestDto> getUserRequestsInForeignEvents(Long userId);

    List<ParticipationRequestDto> getUserRequestsInEvent(Long userId, Long eventId);

    ParticipationRequestDto cancelRequestByUser(Long userId, Long requestId);

    EventRequestStatusUpdateResult confirmOrRejectRequestsByUser(Long userId, Long eventId, EventRequestStatusUpdateRequest
            request);
}
