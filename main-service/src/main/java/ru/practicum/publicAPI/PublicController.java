package ru.practicum.publicAPI;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.categories.dto.ResponseCategoryDto;
import ru.practicum.categories.service.CategoriesService;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.service.EventService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class PublicController {

    private final CategoriesService categoriesService;

    private final EventService eventService;

    @GetMapping("/categories")
    public List<ResponseCategoryDto> getCategories(@RequestParam(name = "from", defaultValue = "0") Integer from,
                                                   @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return categoriesService.getCategories(from, size);
    }

    @GetMapping("/categories/{catId}")
    public ResponseCategoryDto getCategoryById(@PathVariable Long catId) {
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
                                                     String sort, @RequestParam(name = "from", defaultValue = "0")
                                                     Integer from, @RequestParam(name = "size", defaultValue = "10")
                                                     Integer size) {
        return eventService.getEventsFiltered(text, categoriesIds, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }
}
