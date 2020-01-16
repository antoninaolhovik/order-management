package com.amakedon.om.controller;

import com.amakedon.om.data.dto.ProductDto;
import com.amakedon.om.data.model.Product;
import com.amakedon.om.exception.ResourceNotFoundException;
import com.amakedon.om.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private ProductService productService;

    private ModelMapper modelMapper;

    @Autowired
    public ProductController(ProductService productService, ModelMapper modelMapper) {
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public Iterable findAll() {
        List<Product> products = productService.findAll();
        return products.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ProductDto findOne(@PathVariable Long id) {
        return convertToDto(productService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDto create(@RequestBody ProductDto productDto) {
        Product product = convertToEntity(productDto);
        return convertToDto(productService.save(product));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        if (productService.findById(id) == null) {
            throw new ResourceNotFoundException("Product with id " + id + "not found");
        }
        productService.deleteById(id);
    }

    @PutMapping("/{id}")
    public ProductDto update(@RequestBody ProductDto productDto, @PathVariable Long id) {
        Product product = productService.findById(id);
        if (product == null) {
            throw new ResourceNotFoundException("Product with id " + id + "not found");
        }
        product.setSku(productDto.getSku());
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        return convertToDto(productService.save(product));
    }

    private ProductDto convertToDto(Product post) {
        return modelMapper.map(post, ProductDto.class);
    }

    private Product convertToEntity(ProductDto productDto) {
        Product product = modelMapper.map(productDto, Product.class);
        if (product.getCategory() != null) {
            product.getCategory().addProduct(product);
        }


/*        if (productDto.getId() != null) {
            Product oldProduct = productService.findById(productDto.getId());
        }*/
        return product;
    }
}
