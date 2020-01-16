package com.amakedon.om.controller;

import com.amakedon.om.data.dto.CategoryDto;
import com.amakedon.om.data.dto.ProductDto;
import com.amakedon.om.data.model.Category;
import com.amakedon.om.data.model.Product;
import com.amakedon.om.exception.ResourceNotFoundException;
import com.amakedon.om.service.CategoryService;
import com.amakedon.om.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private CategoryService categoryService;

    private ProductService productService;

    private ModelMapper modelMapper;

    @Autowired
    public CategoryController(CategoryService categoryService, ProductService productService, ModelMapper modelMapper) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public Iterable findAll() {
        List<Category> categories = categoryService.findAll();
        return categories.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public CategoryDto findOne(@PathVariable Long id) {
        return convertToDto(categoryService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto create(@RequestBody CategoryDto categoryDto) {
        validateProductExistence(categoryDto.getProducts());
        Category category = convertToEntity(categoryDto);
        return convertToDto(categoryService.save(category));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        if (categoryService.findById(id) == null) {
            throw new ResourceNotFoundException("Category with id " + id + "not found");
        }
        categoryService.deleteById(id);
    }

    @PutMapping("/{id}")
    public CategoryDto update(@RequestBody CategoryDto categoryDto, @PathVariable Long id) {
        if (categoryService.findById(id) == null) {
            throw new ResourceNotFoundException("Category with id " + id + "not found");
        }
        validateProductExistence(categoryDto.getProducts());
        Category category = convertToEntity(categoryDto);
        category.setId(id);
        return convertToDto(categoryService.save(category));
    }

    private void validateProductExistence(List<ProductDto> products) {
        //Check if null or not??
        List<ProductDto> list = products
                .stream()
                .filter(product -> Objects.isNull(productService.findById(product
                        .getId())))
                .collect(Collectors.toList());

        if (!CollectionUtils.isEmpty(list)) {
            new ResourceNotFoundException("Product not found"); //Add ids
        }
    }

    private CategoryDto convertToDto(Category category) {
        return modelMapper.map(category, CategoryDto.class);
    }

    private Category convertToEntity(CategoryDto productDto) {
        Category category = modelMapper.map(productDto, Category.class);
        List<Product> products = category.getProducts()
                .stream()
                .map(p -> {
                    Product p1 = productService.findById(p.getId());
                    p1.setCategory(category);
                    return p1;
                })
                .collect(Collectors.toList());
        System.out.println("PRODUCTS " + products);
        category.setProducts(products);
        return category;
    }
}
