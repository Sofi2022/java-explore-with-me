package ru.practicum.categories.service;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.categories.CategoryDto;
import ru.practicum.categories.NewCategoryDto;
import ru.practicum.categories.ResponseCategoryDto;
import ru.practicum.categories.mapper.CategoriesMapper;
import ru.practicum.categories.model.Category;
import ru.practicum.categories.repository.CategoriesRepository;
import ru.practicum.events.model.Event;
import ru.practicum.events.repository.EventsRepository;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.RequestAlreadyExists;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoriesServiceImpl implements CategoriesService {

    private final CategoriesMapper categoriesMapper = Mappers.getMapper(CategoriesMapper.class);
    private final CategoriesRepository categoriesRepository;

    private final EventsRepository eventsRepository;

    @Transactional
    @Override
    public ResponseCategoryDto addCategory(NewCategoryDto category) {
        Optional<List<Category>> catWithSameName = categoriesRepository.findByName(category.getName());
        if (catWithSameName.get().size() != 0) {
            throw new RequestAlreadyExists("Категория с таким именем уже существует: " + category.getName());
        }
        Category savedCategory = categoriesRepository.save(categoriesMapper.toCategory(category));
        return categoriesMapper.toDto(savedCategory);
    }


    @Transactional
    @Override
    public void deleteCategory(Long catId) {
        Optional<List<Event>> eventsWithSameCat = eventsRepository.findAllByCategoryId(catId);
        if (eventsWithSameCat.get().size() != 0) {
            throw new RequestAlreadyExists("Нельзя удалить категорию, если с ней связаны события " + catId);
        }
        Category category = categoriesRepository.findById(catId).orElseThrow(() ->
                new NotFoundException("Такой категории нет " + catId));
        categoriesRepository.deleteById(catId);
    }


    @Transactional
    @Override
    public ResponseCategoryDto update(CategoryDto category, Long catId) {
        Category savedCategory = categoriesRepository.findById(catId).orElseThrow(() ->
                new NotFoundException("Такой категории нет " + catId));
        Optional<List<Category>> catWithSameName = categoriesRepository.findByName(category.getName());
        if (catWithSameName.get().size() != 0) {
            throw new RequestAlreadyExists("Категория с таким именем уже существует: " + category.getName());
        } else {
            savedCategory.setName(category.getName());
            Category updatedCategory = categoriesRepository.save(savedCategory);
            return categoriesMapper.toDto(updatedCategory);
        }
    }


    @Override
    public List<ResponseCategoryDto> getCategories(Integer from, Integer size) {
        if (size != 0) {
            int page = from / size;
            final PageRequest pageRequest = PageRequest.of(page, size);
            Page<Category> result = categoriesRepository.findAll(pageRequest);
            List<Category> categories = result.getContent();
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
