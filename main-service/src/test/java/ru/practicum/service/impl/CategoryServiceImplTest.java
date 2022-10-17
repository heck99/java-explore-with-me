package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.practicum.dto.CategoryDto;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.CategoryMapper;
import ru.practicum.model.Category;
import ru.practicum.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    CategoryServiceImpl service;

    @Mock
    CategoryRepository categoryRepository;

    CategoryMapper categoryMapper;

    @BeforeEach
    void init() {
        service = new CategoryServiceImpl(categoryRepository, categoryMapper);
    }

    Category category1 = new Category(1, "category1");
    Category category2 = new Category(2, "category2");
    Category category3 = new Category(3, "category3");
    CategoryDto newCategory = new CategoryDto(null, "user1");

    @Test
    public void testGetCategoryByIdCorrect() {
        when(categoryRepository.findById(any())).thenReturn(Optional.of(category1));
        CategoryDto dto = service.getCategoryByIdOrThrow(1);
        assertEquals(1, dto.getId());
        verify(categoryRepository, times(1)).findById(any());
    }

    @Test
    public void testGetCategoryByIdNotFound() {
        when(categoryRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.getCategoryByIdOrThrow(1));
        verify(categoryRepository, times(1)).findById(any());
    }

    @Test
    public void createCategoryCorrect() {
        when(categoryRepository.save(any())).thenReturn(category1);
        CategoryDto categoryDto = service.createCategory(newCategory);
        assertEquals(categoryDto.getId(), 1);
        verify(categoryRepository, times(1)).save(any());
    }

    @Test
    public void updateCategoryCorrect() {
        Category updateCategory = new Category(1, "new category1");
        CategoryDto updateCategoryDto = new CategoryDto(1, "new category1");
        when(categoryRepository.save(any())).thenReturn(updateCategory);
        when(categoryRepository.findById(any())).thenReturn(Optional.of(category1));
        CategoryDto categoryDto = service.updateCategory(updateCategoryDto);
        assertEquals(categoryDto.getId(), 1);
        assertEquals(categoryDto.getName(), "new category1");
        verify(categoryRepository, times(1)).save(any());
        verify(categoryRepository, times(1)).findById(any());
    }

    @Test
    public void updateCategoryNullName() {
        Category updateCategory = new Category(1, "category1");
        CategoryDto updateCategoryDto = new CategoryDto(1, null);
        when(categoryRepository.save(any())).thenReturn(updateCategory);
        when(categoryRepository.findById(any())).thenReturn(Optional.of(category1));
        CategoryDto categoryDto = service.updateCategory(updateCategoryDto);
        assertEquals(categoryDto.getId(), 1);
        assertEquals(categoryDto.getName(), "category1");
        verify(categoryRepository, times(1)).save(any());
        verify(categoryRepository, times(1)).findById(any());
    }

    @Test
    public void updateCategoryNotFound() {
        CategoryDto updateCategoryDto = new CategoryDto(1, "new category1");
        when(categoryRepository.findById(any())).thenThrow(NotFoundException.class);
        assertThrows(NotFoundException.class, () -> service.updateCategory(updateCategoryDto));
        verify(categoryRepository, times(1)).findById(any());
    }

    @Test
    public void testDeleteCategoryCorrect() {
        when(categoryRepository.findById(any())).thenReturn(Optional.of(category1));
        service.deleteCategory(1);
        verify(categoryRepository, times(1)).deleteById(any());
        verify(categoryRepository, times(1)).findById(any());
    }

    @Test
    public void testDeleteCategoryNotFound() {
        when(categoryRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.deleteCategory(1));
        verify(categoryRepository, times(1)).findById(any());
    }

    @Test
    public void testGetAllCategory() {
        when(categoryRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(category1, category2, category3)));
        List<CategoryDto> list = service.getAllCategories(0, 10);
        assertEquals(list.get(0).getId(), 1);
        assertEquals(list.get(1).getId(), 2);
        assertEquals(list.get(2).getId(), 3);
        verify(categoryRepository, times(1)).findAll(any(Pageable.class));
    }

}