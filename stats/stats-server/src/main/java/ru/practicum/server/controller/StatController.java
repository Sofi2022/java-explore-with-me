package ru.practicum.server.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.EndpointHitDto;
import ru.practicum.ViewStateDto;
import ru.practicum.server.service.StatService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class StatController {

    private final StatService service;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public EndpointHitDto createEndpoint(@RequestBody @Valid EndpointHitDto endpointHitDto) {
        return service.createEndpoint(endpointHitDto);
    }

    @GetMapping("/stats")
    public List<ViewStateDto> getStats(@RequestParam String start,
                                       @RequestParam String end,
                                       @RequestParam(required = false) List<String> uris,
                                       @RequestParam(defaultValue = "false") boolean unique) {
        return service.getStats(start, end, uris, unique);
    }
}
