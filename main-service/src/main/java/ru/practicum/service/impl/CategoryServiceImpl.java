package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.dto.CategoryDto;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.CategoryMapper;
import ru.practicum.model.Category;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.service.CategoryService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryDto createCategory(CategoryDto category) {
        return categoryMapper.toCategoryDto(categoryRepository.save(categoryMapper.fromCategoryDto(category)));
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categorydto) {
        Category category = categoryRepository.findById(categorydto.getId())
                .orElseThrow(() -> new NotFoundException(String.format("Категория с id = %d не найдена", categorydto.getId())));
        if (category.getName() != null) {
            category.setName(categorydto.getName());
        }
        return categoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Override
    public void deleteCategory(int categoryId) {
        getCategoryByIdOrThrow(categoryId);
        categoryRepository.deleteById(categoryId);
    }

    @Override
    public CategoryDto getCategoryByIdOrThrow(int categoryId) {
        return categoryMapper.toCategoryDto(categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(String.format("Категория с id = %d не найдена", categoryId))));
    }

    @Override
    public List<CategoryDto> getAllCategories(int from, int size) {
        return categoryRepository.findAll(PageRequest.of(from / size, size)).stream()
                .map(categoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }
}
