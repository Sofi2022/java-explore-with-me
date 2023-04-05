package ru.practicum.categories.service;

import ru.practicum.categories.CategoryDto;
import ru.practicum.categories.ResponseCategoryDto;
import ru.practicum.categories.NewCategoryDto;

import java.util.List;

public interface CategoriesService {

    ResponseCategoryDto addCategory(NewCategoryDto category);

    void deleteCategory(Long catId);

    ResponseCategoryDto update(CategoryDto category, Long catId);

    List<ResponseCategoryDto> getCategories(Integer from, Integer size);

    ResponseCategoryDto getCategoryById(Long catId);
}
