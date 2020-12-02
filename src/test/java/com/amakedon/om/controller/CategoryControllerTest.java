package com.amakedon.om.controller;

import com.amakedon.om.data.model.Category;
import com.amakedon.om.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.List;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@ActiveProfiles("h2")
@AutoConfigureMockMvc
public class CategoryControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CategoryService categoryService;

    @Test
    public void whenPostCategory_thenCreateCategory() throws Exception {
        Category category = createTestCategory(1L);


        given(categoryService.save(Mockito.any())).willReturn(category);

        mvc.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(category)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(category.getName()));

        verify(categoryService, VerificationModeFactory.times(1)).save(Mockito.any());
        reset(categoryService);
    }

    @Test
    public void whenGetCategories_thenReturnJsonArray() throws Exception {
        Category category = createTestCategory(1L);

        List<Category> allCategories = Collections.singletonList(category);

        given(categoryService.findAll()).willReturn(allCategories);

        mvc.perform(get("/api/categories")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value(category.getName()));
    }

    @Test
    public void whenGetCategory_thenReturnCategory() throws Exception {
        long categoryId = 1L;

        Category category = createTestCategory(categoryId);

        given(categoryService.findById(categoryId)).willReturn(category);

        mvc.perform(get("/api/categories/" + categoryId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(category.getName()));
    }

    @Test
    public void whenUpdateCategory_thenUpdateCategory() throws Exception {
        long categoryId = 1L;
        Category category = createTestCategory(categoryId);

        mvc.perform(put("/api/categories/" + categoryId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(category)))
                .andExpect(status().isOk());
        verify(categoryService, VerificationModeFactory.times(1))
                .update(Mockito.any());
    }


    @Test
    public void whenDeleteCategory_thenDeleteCategory() throws Exception {
        long categoryId = 1L;

        mvc.perform(delete("/api/categories/" + categoryId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(categoryService, VerificationModeFactory.times(1))
                .deleteById(categoryId);
    }

    private Category createTestCategory(long categoryId) {
        Category category = new Category();
        String name = randomAlphabetic(6);
        category.setName(name);
        category.setId(categoryId);
        return category;
    }

}