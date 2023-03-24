package ru.practicum.categories.service;

import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.dto.ResponseCategoryDto;
import ru.practicum.categories.dto.NewCategoryDto;

import java.util.List;

public interface CategoriesService {

    ResponseCategoryDto addCategory(NewCategoryDto category);

    void deleteCategory(Long catId);

    ResponseCategoryDto update(CategoryDto category, Long catId);

    List<ResponseCategoryDto> getCategories(Integer from, Integer size);

    ResponseCategoryDto getCategoryById(Long catId);
}
