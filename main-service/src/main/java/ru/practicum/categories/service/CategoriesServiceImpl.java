package ru.practicum.categories.service;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.dto.ResponseCategoryDto;
import ru.practicum.categories.dto.NewCategoryDto;
import ru.practicum.categories.mapper.CategoriesMapper;
import ru.practicum.categories.model.Category;
import ru.practicum.categories.repository.CategoriesRepository;
import ru.practicum.user.exception.NotFoundException;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriesServiceImpl implements CategoriesService {

    private final CategoriesMapper categoriesMapper = Mappers.getMapper(CategoriesMapper.class);
    private final CategoriesRepository categoriesRepository;

    @Override
    public ResponseCategoryDto addCategory(NewCategoryDto category) {
        Category savedCategory =  categoriesRepository.save(categoriesMapper.toCategory(category));
        System.out.println("savedCat= " + categoriesMapper.toDto(savedCategory));
        return categoriesMapper.toDto(savedCategory);
    }


    @Override
    public void deleteCategory(Long catId) {
        Category category = categoriesRepository.findById(catId).orElseThrow(() ->
                new NotFoundException("Такой категории нет " + catId));
        System.out.println("category to delete= " + category + category.getId());
        categoriesRepository.deleteById(catId);
    }


    @Override
    public ResponseCategoryDto update(CategoryDto category, Long catId) {
        Category savedCategory = categoriesRepository.findById(catId).orElseThrow(() ->
                new NotFoundException("Такой категории нет " + catId));
        savedCategory.setName(category.getName());
        Category updatedCategory = categoriesRepository.save(savedCategory);
        return categoriesMapper.toDto(updatedCategory);
    }


    @Override
    public List<ResponseCategoryDto> getCategories(Integer from, Integer size) {
        if (size != 0) {
            int page = from / size;
            final PageRequest pageRequest = PageRequest.of(page, size);
            Page<Category> result = categoriesRepository.findAll(pageRequest);
            List<Category> categories = result.getContent();
//            if(categories.size() == 0) {
//                return result.
//            }
            return categoriesMapper.toListDto(categories);
        }
        List<Category> categories = categoriesRepository.findAll();
        return categoriesMapper.toListDto(categories);
    }


    @Override
    public ResponseCategoryDto getCategoryById(Long catId) {
        Category category = categoriesRepository.findById(catId).orElseThrow(() ->
                new NotFoundException("Такой категории нет " + catId));
        return categoriesMapper.toDto(category);
    }

}
