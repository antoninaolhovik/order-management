package com.amakedon.om.service;

import com.amakedon.om.data.model.Category;
import com.amakedon.om.data.repository.jpa.CategoryRepository;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ActiveProfiles("h2")
@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private static CategoryRepository categoryRepository;

    @InjectMocks
    private static CategoryService categoryService = new CategoryServiceImpl(categoryRepository);

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindCategory() {

        long categoryId = 1L;

        Category category = new Category();
        category.setName(randomAlphabetic(6));


        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        Category retrievedCategory = categoryService.findById(categoryId);

        assertSame(category, retrievedCategory);

    }

    @Test
    public void testFindAllCategories() {

        Category category1 = new Category();
        category1.setName(randomAlphabetic(6));

        Category category2 = new Category();
        category2.setName(randomAlphabetic(6));

        Category category3 = new Category();
        category3.setName(randomAlphabetic(6));

        List<Category> categoriesList = Arrays.asList(category1, category2, category3);


        Mockito.when(categoryRepository.findAll()).thenReturn(categoriesList);

        List<Category> retrievedCategories = categoryService.findAll();

        assertIterableEquals(categoriesList, retrievedCategories);
    }


    @Test
    public void testSaveCategory() {

        Category category = new Category();
        category.setName(randomAlphabetic(6));

        categoryService.save(category);
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    public void testUpdateCategory() {

        Long categoryId = 1L;

        Category category = new Category();
        category.setId(categoryId);
        category.setName(randomAlphabetic(6));

        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        String updatedName = randomAlphabetic(6);
        category.setName(updatedName);
        Mockito.when(categoryRepository.save(category)).thenReturn(category);

        categoryService.update(category);

    }

    @Test
    public void testDeleteCategory() {
        Long categoryId = 1L;

        Category category = new Category();
        category.setName(randomAlphabetic(6));

        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        categoryService.deleteById(categoryId);
        verify(categoryRepository, times(1)).deleteById(categoryId);
    }

}