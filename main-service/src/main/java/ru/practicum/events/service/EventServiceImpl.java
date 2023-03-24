package ru.practicum.events.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.events.dto.Event;
import ru.practicum.events.dto.NewEventDto;
import ru.practicum.events.mapper.EventsMapper;
import ru.practicum.events.repository.EventsRepository;
import ru.practicum.user.exception.NotFoundException;
import ru.practicum.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final UserRepository userRepository;

    private final EventsRepository eventsRepository;

    private final EventsMapper eventsMapper;


    @Override
    public Event createEvent(Long userId, NewEventDto event) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Такого пользователя нет " + userId));
        Event eventToSave = eventsMapper.fromDto(event, userId);
        eventsRepository.save(eventToSave);
        return null;
    }
}
