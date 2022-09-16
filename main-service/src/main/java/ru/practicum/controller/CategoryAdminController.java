package ru.practicum.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CategoryDto;
import ru.practicum.service.CategoryService;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/categories")
@AllArgsConstructor
public class CategoryAdminController {

    private final CategoryService categoryService;

    @PostMapping
    public CategoryDto createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        return categoryService.createCategory(categoryDto);
    }

    @DeleteMapping("{categoryId}")
    public void deleteCategory(@PathVariable int categoryId) {
        categoryService.deleteCategory(categoryId);
    }

    @PatchMapping
    public CategoryDto updateCategory(@Valid @RequestBody CategoryDto categoryDto) {
        return categoryService.updateCategory(categoryDto);
    }

}
