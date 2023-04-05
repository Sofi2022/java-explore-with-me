package ru.practicum.privateAPI;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.EventFullDto;
import ru.practicum.events.EventShortDto;
import ru.practicum.events.NewEventDto;
import ru.practicum.events.UpdateEventUserRequest;
import ru.practicum.events.service.EventService;
import ru.practicum.requests.EventRequestStatusUpdateRequest;
import ru.practicum.requests.EventRequestStatusUpdateResult;
import ru.practicum.requests.ParticipationRequestDto;
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
        log.info("Private: Вызван метод createEvent, userId {} ", userId);
        return eventService.createEvent(userId, event);
    }


    @PostMapping("users/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto addRequest(@PathVariable Long userId, @RequestParam(name = "eventId") Long eventId) {
        log.info("Private: Вызван метод addRequest, userId {}", userId);
        ParticipationRequestDto result = requestService.addRequest(userId, eventId);
        System.out.println("RESULT= " + result);
        return result;
    }

    @GetMapping("users/{userId}/events/{eventId}")
    public EventFullDto getEventByUser(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("Private: Вызван метод getEventByUser, userId, eventId {} {}", userId, eventId);
        return eventService.getEventByUser(userId, eventId);
    }

    @GetMapping("users/{userId}/events")
    public List<EventShortDto> getEventsByUserWithPage(@PathVariable Long userId, @RequestParam(name = "from",
            defaultValue = "0") Integer from, @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Private: Вызван метод getEventsByUserWithPage, userId, from, size {} {} {}", userId, from, size);
        return eventService.getEventsByUserWithPage(userId, from, size);
    }


    @GetMapping("users/{userId}/requests")
    List<ParticipationRequestDto> getUserRequestsInForeignEvents(@PathVariable Long userId) {
        log.info("Private: Вызван метод getUserRequestsInForeignEvents, userId {}", userId);
        return requestService.getUserRequestsInForeignEvents(userId);
    }

    @GetMapping("users/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getUserRequestsInEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("Private: Вызван метод getUserRequestsInEvent, userId eventId {} {}", userId, eventId);
        return requestService.getUserRequestsInEvent(userId, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    public EventFullDto updateEventByUser(@PathVariable Long userId, @PathVariable Long eventId, @Valid @RequestBody
    UpdateEventUserRequest event) {
        log.info("Private: Вызван метод updateEventByUser, userId eventId {} {}", userId, eventId);
        return eventService.updateEventByUser(userId, eventId, event);
    }


    @PatchMapping("/users/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelRequestByUser(@PathVariable Long userId, @PathVariable Long requestId) {
        log.info("Private: Вызван метод cancelRequestByUser, userId requestId {} {}", userId, requestId);
        return requestService.cancelRequestByUser(userId, requestId);
    }


    @PatchMapping("users/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult confirmOrRejectRequestsByUser(@PathVariable Long userId, @PathVariable
    Long eventId, @Valid @RequestBody EventRequestStatusUpdateRequest request) {
        log.info("Private: Вызван метод confirmOrRejectRequestsByUser, userId eventId {} {}", userId, eventId);
        return requestService.confirmOrRejectRequestsByUser(userId, eventId, request);
    }
}
