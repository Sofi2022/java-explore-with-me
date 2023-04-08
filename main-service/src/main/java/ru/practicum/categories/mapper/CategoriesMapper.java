package ru.practicum.categories.mapper;

import org.mapstruct.Mapper;
import ru.practicum.categories.CategoryDto;
import ru.practicum.categories.ResponseCategoryDto;
import ru.practicum.categories.NewCategoryDto;
import ru.practicum.categories.model.Category;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoriesMapper {

    ResponseCategoryDto toDto(Category category);

    Category toCategory(NewCategoryDto category);

    List<ResponseCategoryDto> toListDto(List<Category> categories);

    CategoryDto toCatDto(Category category);
}
