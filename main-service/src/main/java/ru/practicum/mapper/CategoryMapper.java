package ru.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.dto.CategoryDto;
import ru.practicum.model.Category;

@Component
public class CategoryMapper {
    public Category fromCategoryDto(CategoryDto dto) {
        return new Category(dto.getId(), dto.getName());
    }

    public CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }
}
