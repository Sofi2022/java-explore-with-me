package ru.practicum.requests.service;

import ru.practicum.requests.dto.ParticipationRequestDto;

public interface RequestService {

    ParticipationRequestDto addRequest(Long userId, Long eventId);
}
