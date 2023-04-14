package ru.practicum.publicAPI;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.StateClient;
import ru.practicum.categories.ResponseCategoryDto;
import ru.practicum.categories.service.CategoriesService;
import ru.practicum.comment.CommentDto;
import ru.practicum.comment.service.CommentService;
import ru.practicum.compilations.CompilationDto;
import ru.practicum.compilations.service.CompilationService;
import ru.practicum.events.EventFullDto;
import ru.practicum.events.EventShortDto;
import ru.practicum.events.model.EventSort;
import ru.practicum.events.service.EventService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class PublicController {

    private final CategoriesService categoriesService;

    private final EventService eventService;

    private final CompilationService compilService;

    private final CommentService commentService;

    private final StateClient stateClient;

    @GetMapping("/categories")
    public List<ResponseCategoryDto> getCategories(@RequestParam(name = "from", defaultValue = "0") Integer from,
                                                   @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Public: Вызван метод getCategories, from, size {} {}", from, size);
        return categoriesService.getCategories(from, size);
    }

    @GetMapping("/categories/{catId}")
    public ResponseCategoryDto getCategoryById(@PathVariable Long catId) {
        log.info("Public: Вызван метод getCategoryById, catId {}", catId);
        return categoriesService.getCategoryById(catId);
    }


    @GetMapping("events")
    public List<EventShortDto> getEventsFiltered(@RequestParam(name = "text", required = false) String text,
                                                 @RequestParam(name = "categories", required = false) List<Long> categoriesIds,
                                                 @RequestParam(name = "paid", required = false) Boolean paid,
                                                 @RequestParam(name = "rangeStart", required = false) String rangeStart,
                                                 @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
                                                 @RequestParam(name = "onlyAvailable", defaultValue = "false")
                                                     Boolean onlyAvailable, @RequestParam(name = "sort", required = false)
                                                     EventSort sorted, @RequestParam(name = "from", defaultValue = "0")
                                                     Integer from, @RequestParam(name = "size", defaultValue = "10")
                                                     Integer size, HttpServletRequest request) {
        log.info("Public: Вызван метод getEventsFiltered");
        List<EventShortDto> result =  eventService.getEventsFiltered(text, categoriesIds, paid, rangeStart, rangeEnd,
                onlyAvailable, sorted, from, size, request);
        stateClient.postHit("ewm-main-service", request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now());
        return result;

    }


    @GetMapping("events/{id}")
    public EventFullDto getFullEventById(@PathVariable Long id, HttpServletRequest request) {
        log.info("Public: Вызван метод getCategoryById, id {}", id);
        log.info("client ip: {}", request.getRemoteAddr());
        log.info("endpoint path: {}", request.getRequestURI());
        EventFullDto event = eventService.getFullEventById(id, request);
        stateClient.postHit("ewm-main-service", request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now());
        return event;
    }


    @GetMapping("compilations/{compId}")
    public CompilationDto getCompilById(@PathVariable Long compId) {
        log.info("Public: Вызван метод getCompilById, id {}", compId);
        return compilService.getCompilById(compId);
    }


    @GetMapping("compilations")
    public List<CompilationDto> getCompilsWithParams(@RequestParam(name = "pinned", required = false) Boolean pinned,
                                        @RequestParam(name = "from", defaultValue = "0")
    Integer from, @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Public: Вызван метод getCompilsWithParams pinned {} from {} size {} ", pinned, from, size);
        return compilService.getCompilsWithParams(pinned, from, size);
    }


    @GetMapping("/comments")
    public List<CommentDto> getAllComments() {
        return commentService.getAllComments();
    }
}
