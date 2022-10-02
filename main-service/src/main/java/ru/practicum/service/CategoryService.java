package ru.practicum.service;

import ru.practicum.dto.CategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto createCategory(CategoryDto category);

    CategoryDto updateCategory(CategoryDto category);

    void deleteCategory(int categoryId);

    CategoryDto getCategoryByIdOrThrow(int categoryId);

    List<CategoryDto> getAllCategories(int from, int size);
}
