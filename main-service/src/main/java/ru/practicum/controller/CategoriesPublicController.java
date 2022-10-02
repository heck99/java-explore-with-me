package ru.practicum.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
        return categoryService.getCategoryByIdOrThrow(categoryId);
    }

    @GetMapping()
    public List<CategoryDto> getAllCategories(@RequestParam(name = "from", defaultValue = "0") int from,
                                              @RequestParam(name = "size", defaultValue = "10") int size) {
        return categoryService.getAllCategories(from, size);
    }
}
