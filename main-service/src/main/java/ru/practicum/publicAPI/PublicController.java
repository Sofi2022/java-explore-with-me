package ru.practicum.publicAPI;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.categories.dto.ResponseCategoryDto;
import ru.practicum.categories.service.CategoriesService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class PublicController {

    private final CategoriesService categoriesService;

    @GetMapping("/categories")
    public List<ResponseCategoryDto> getCategories(@RequestParam(name = "from", defaultValue = "0") Integer from,
                                                   @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return categoriesService.getCategories(from, size);
    }


    @GetMapping("/categories/{catId}")
    public ResponseCategoryDto getCategoryById(@PathVariable Long catId) {
        return categoriesService.getCategoryById(catId);
    }
}
