package ru.practicum.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CategoryDto;
import ru.practicum.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/categories")
@AllArgsConstructor
public class CategoriesPublicController {

    private final CategoryService categoryService;

    @GetMapping("{categoryId}")
    public CategoryDto getCategoryById(@PathVariable int categoryId) {
        return categoryService.getCategoryById(categoryId);
    }

    @GetMapping()
    public List<CategoryDto> getAllCategories(@RequestParam(name = "from", defaultValue = "0") int from,
                                              @RequestParam(name = "size", defaultValue = "10") int size) {
        return categoryService.getAllCategories(from, size);
    }
}
