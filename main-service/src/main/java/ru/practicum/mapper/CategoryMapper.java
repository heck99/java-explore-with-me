package ru.practicum.mapper;

import ru.practicum.dto.CategoryDto;
import ru.practicum.model.Category;

public class CategoryMapper {
    public Category fromCategoryDto(CategoryDto dto) {
        return new Category(dto.getId(), dto.getName());
    }

    public CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }
}
