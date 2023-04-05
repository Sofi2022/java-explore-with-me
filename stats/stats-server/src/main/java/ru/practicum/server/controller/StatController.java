package ru.practicum.server.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stat.EndpointHitDto;
import ru.practicum.stat.ViewStateDto;
import ru.practicum.server.service.StatService;

import javax.validation.Valid;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class StatController {

    public static final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIME_PATTERN);
    private final StatService service;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public EndpointHitDto createEndpoint(@RequestBody @Valid EndpointHitDto endpointHitDto) {
        return service.createEndpoint(endpointHitDto);
    }

    @GetMapping("/stats")
    public List<ViewStateDto> getStats(@RequestParam(name = "start") String start,
                                       @RequestParam(name = "end") String end,
                                       @RequestParam(name = "uris", required = false) List<String> uris,
                                       @RequestParam(name = "unique", defaultValue = "false") Boolean unique) {
        return service.getStats(LocalDateTime.parse(URLDecoder.decode(start, StandardCharsets.UTF_8), formatter),
                LocalDateTime.parse(URLDecoder.decode(end, StandardCharsets.UTF_8), formatter), uris, unique);
    }
}
