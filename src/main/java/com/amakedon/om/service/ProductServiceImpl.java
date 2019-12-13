package com.amakedon.om.service;

import com.amakedon.om.data.exception.EntityNotFoundException;
import com.amakedon.om.data.model.Product;
import com.amakedon.om.data.repository.jpa.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product findById(long id) {
        return productRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void update(Product product) {
        productRepository.findById(product.getId())
                .orElseThrow(EntityNotFoundException::new);
        productRepository.save(product);
    }

    @Override
    public void deleteById(long id) {
        productRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
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
}
