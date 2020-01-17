package com.amakedon.om.service;

import com.amakedon.om.data.model.Product;

import java.util.List;

public interface ProductService {

    Product findById(long id);

    Product save(Product product);

    void update(Product product);

    void deleteById(long id);

    List<Product> findAll();

    void deleteAll();

    void validateProductExistence(List<Product> products);
}
