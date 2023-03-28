package ru.practicum.categories.mapper;

import org.mapstruct.Mapper;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.dto.ResponseCategoryDto;
import ru.practicum.categories.dto.NewCategoryDto;
import ru.practicum.categories.model.Category;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoriesMapper {

    ResponseCategoryDto toDto(Category category);

    Category toCategory(NewCategoryDto category);

    List<ResponseCategoryDto> toListDto(List<Category> categories);

    CategoryDto toCatDto(Category category);
}
