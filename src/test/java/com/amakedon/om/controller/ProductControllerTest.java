package com.amakedon.om.controller;

import com.amakedon.om.data.model.Product;
import com.amakedon.om.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
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
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProductService productService;

    @Test
    public void whenPostProduct_thenCreateProduct() throws Exception {
        Product product = createTestProduct(1L);

        given(productService.save(Mockito.any())).willReturn(product);

        mvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(product)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(product.getName()))
                .andExpect(jsonPath("$.sku").value(product.getSku()))
                .andExpect(jsonPath("$.price").value(product.getPrice()));

        verify(productService, VerificationModeFactory.times(1)).save(Mockito.any());
        reset(productService);
    }

    @Test
    public void whenGetProducts_thenReturnJsonArray() throws Exception {
        Product product = createTestProduct(1L);

        List<Product> allProducts = Collections.singletonList(product);

        given(productService.findAll()).willReturn(allProducts);

        mvc.perform(get("/api/products")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value(product.getName()));
    }

    @Test
    public void whenGetProduct_thenReturnProduct() throws Exception {
        long productId = 1L;

        Product product = createTestProduct(productId);

        given(productService.findById(productId)).willReturn(product);

        mvc.perform(get("/api/products/" + productId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(product.getName()));
    }

    @Test
    public void whenUpdateProduct_thenUpdateProduct() throws Exception {
        long productId = 1L;
        Product product = createTestProduct(productId);
        given(productService.findById(productId)).willReturn(product);


        mvc.perform(put("/api/products/" + productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(product)))
                .andExpect(status().isOk());
        verify(productService, VerificationModeFactory.times(1))
                .update(Mockito.any());
    }


    @Test
    public void whenDeleteProduct_thenDeleteProduct() throws Exception {
        long productId = 1L;

        mvc.perform(delete("/api/products/" + productId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(productService, VerificationModeFactory.times(1))
                .deleteById(productId);
    }

    private Product createTestProduct(long productId) {
        Product product = new Product();
        String name = randomAlphabetic(6);
        product.setName(name);
        product.setSku(name);
        product.setPrice(BigDecimal.TEN);
        product.setId(productId);
        return product;
    }
}