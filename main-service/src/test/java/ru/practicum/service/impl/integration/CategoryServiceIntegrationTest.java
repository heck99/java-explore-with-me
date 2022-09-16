package ru.practicum.service.impl.integration;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.CategoryDto;
import ru.practicum.exception.NotFound;
import ru.practicum.model.Category;
import ru.practicum.service.CategoryService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
@AllArgsConstructor(onConstructor_ = @Autowired)
public class CategoryServiceIntegrationTest {

    private final EntityManager em;
    private final CategoryService service;


    @Test
    public void getAllCorrect() {
        List<CategoryDto> categories = service.getAllCategories(0, 10);
        assertEquals(categories.size(), 3);
    }

    @Test
    public void getAllCorrectWithLowSize() {
        List<CategoryDto> categories = service.getAllCategories(0, 2);
        assertEquals(categories.size(), 2);
    }

    @Test
    public void getByIdCorrect() {
        CategoryDto categoryDto = service.getCategoryById(1);
        assertEquals(categoryDto.getId(), 1);
        assertEquals(categoryDto.getName(), "category1");
    }

    @Test
    public void UpdateCategoryCorrect() {
        CategoryDto categoryDto = service.getCategoryById(1);
        assertEquals(categoryDto.getId(), 1);
        assertEquals(categoryDto.getName(), "category1");

        CategoryDto updateCategory = new CategoryDto(1, "new category name");
        service.updateCategory(updateCategory);

        categoryDto = service.getCategoryById(1);
        assertEquals(categoryDto.getId(), 1);
        assertEquals(categoryDto.getName(), "new category name");
    }

    @Test
    public void getByIdNotFound() {
        assertThrows(NotFound.class, () -> service.getCategoryById(4));
    }

    @Test
    public void deleteCorrect() {
        TypedQuery<Category> query0 = em.createQuery("SELECT c FROM Category c", Category.class);
        List<Category> categories = query0.getResultList();
        assertEquals(categories.size(), 3);

        service.deleteCategory(3);

        categories = query0.getResultList();
        assertEquals(categories.size(), 2);
    }

    @Test
    public void createCorrect() {
        TypedQuery<Category> query0 = em.createQuery("SELECT c FROM Category c", Category.class);
        List<Category> categories = query0.getResultList();
        assertEquals(categories.size(), 3);

        CategoryDto categoryDto = new CategoryDto(null, "category4");
        service.createCategory(categoryDto);

        categories = query0.getResultList();
        assertEquals(categories.size(), 4);

        TypedQuery<Category> query1 = em.createQuery("Select c from Category c WHERE c.id = 4", Category.class);
        Category category = query1.getSingleResult();
        assertEquals(category.getId(), 4);
        assertEquals(category.getName(), "category4");
    }
}
