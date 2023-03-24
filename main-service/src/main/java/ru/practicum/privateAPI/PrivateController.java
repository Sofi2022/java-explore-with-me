package ru.practicum.privateAPI;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.Event;
import ru.practicum.events.dto.NewEventDto;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class PrivateController {

    @PostMapping("users/{userId}/events")
    public Event createEvent(@PathVariable Long userId, @Valid @RequestBody NewEventDto event) {
        return null;
    }

}
