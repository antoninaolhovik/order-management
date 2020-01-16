package com.amakedon.om.data.dto;

import com.amakedon.om.data.model.Product;
import org.modelmapper.ModelMapper;
import org.junit.jupiter.api.Test;

import static org.apache.commons.lang3.RandomStringUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


class ProductDtoUnitTest {

    private ModelMapper modelMapper = new ModelMapper();

    @Test
    void whenConvertPostEntityToPostDto_thenCorrect() {
        Product product = new Product();
        product.setId(1L);
        product.setName(randomAlphabetic(6));
        product.setSku(randomAlphabetic(6));
        product.setPrice(1d);

        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        assertEquals(product.getId(), productDto.getId());
        assertEquals(product.getName(), productDto.getName());
        assertEquals(product.getSku(), productDto.getSku());
        assertEquals(product.getPrice(), productDto.getPrice());

    }

    @Test
    void whenConvertPostDtoToPostEntity_thenCorrect() {
        ProductDto productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setName(randomAlphabetic(6));
        productDto.setSku(randomAlphabetic(6));
        productDto.setPrice(1d);

        Product product = modelMapper.map(productDto, Product.class);
        assertEquals(productDto.getId(), product.getId());
        assertEquals(productDto.getName(), product.getName());
        assertEquals(productDto.getSku(), product.getSku());
        assertEquals(productDto.getPrice(), product.getPrice());
    }
}
