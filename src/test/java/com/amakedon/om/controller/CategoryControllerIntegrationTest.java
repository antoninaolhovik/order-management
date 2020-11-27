package com.amakedon.om.controller;

import com.amakedon.om.data.model.Category;
import com.amakedon.om.service.CategoryService;
import com.amakedon.om.service.ProductService;
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

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@ActiveProfiles("h2")
@AutoConfigureMockMvc
public class CategoryControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    //@Autowired
    //private CategoryController categoryController;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private ProductService productService;

    @Test
    public void whenPostCategory_thenCreateCategory() throws Exception {
        Category category = new Category();
        String name = randomAlphabetic(6);
        category.setName(name);

        given(categoryService.save(Mockito.any())).willReturn(category);

        mvc.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(category)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(name));

        verify(categoryService, VerificationModeFactory.times(1)).save(Mockito.any());
        reset(categoryService);
    }


}