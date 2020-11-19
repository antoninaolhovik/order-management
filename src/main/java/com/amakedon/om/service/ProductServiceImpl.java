package com.amakedon.om.service;

import com.amakedon.om.data.model.Category;
import com.amakedon.om.exception.ResourceNotFoundException;
import com.amakedon.om.data.model.Product;
import com.amakedon.om.data.repository.jpa.CategoryRepository;
import com.amakedon.om.data.repository.jpa.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    private CategoryRepository categoryRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Product findById(long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found"));
    }

    @Override
    @Transactional
    public Product save(Product product) {
        if (product.getCategory() == null) {
            throw new ResourceNotFoundException("Category not found ");
        }
        Category category = categoryRepository.findById(product.getCategory().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Category with id " + product.getCategory().getId() + " not found"));

        product.setCategory(category);

        return productRepository.save(product);
    }

    @Override
    public void update(Product product) {
        findById(product.getId());
        productRepository.save(product);
    }

    @Override
    public void deleteById(long id) {
        findById(id);
        productRepository.deleteById(id);
    }

    @Override
    public List<Product> findAll() {
        List<Product> list = new ArrayList<>();
        productRepository.findAll().forEach(list::add);
        return list;
    }

    @Override
    public void deleteAll() {
        productRepository.deleteAll();
    }

    public void validateProductExistence(List<Product> products) {
        //Check if null or not??
        List<Product> list = products
                .stream()
                .filter(product -> !productRepository.findById(product
                        .getId()).isPresent())
                .collect(Collectors.toList());

        if (!CollectionUtils.isEmpty(list)) {
            throw new ResourceNotFoundException("Products with ids not found:" + list
                    .stream()
                    .map(p -> String.valueOf(p.getId()))
                    .collect(Collectors.joining(",")));
        }
    }



}
