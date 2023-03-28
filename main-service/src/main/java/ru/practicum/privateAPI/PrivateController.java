package ru.practicum.privateAPI;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.model.Event;
import ru.practicum.events.dto.NewEventDto;
import ru.practicum.events.service.EventService;
import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.requests.service.RequestService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class PrivateController {

    private final RequestService requestService;

    private final EventService eventService;


    @PostMapping("users/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@PathVariable Long userId, @Valid @RequestBody NewEventDto event) {
        return eventService.createEvent(userId, event);
    }


    @PostMapping("users/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto addRequest(@PathVariable Long userId, @RequestParam(name = "eventId") Long eventId) {
        return requestService.addRequest(userId, eventId);
    }

    @GetMapping("users/{userId}/events/{eventId}")
    public List<EventFullDto> getEventsByUser(@PathVariable Long userId, @PathVariable Long eventId) {
        return eventService.getEventsByUser(userId, eventId);
    }

    @GetMapping("users/{userId}/events")
    public List<EventShortDto> getEventsByUserWithPage(@PathVariable Long userId, @RequestParam(name = "from",
            defaultValue = "0") Integer from, @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return eventService.getEventsByUserWithPage(userId, from, size);
    }
}
