package com.amakedon.om.service;

import com.amakedon.om.exception.EntityNotFoundException;
import com.amakedon.om.data.model.Product;
import com.amakedon.om.data.repository.jpa.CategoryRepository;
import com.amakedon.om.data.repository.jpa.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
        return productRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    @Transactional
    public Product save(Product product) {
        if (product.getCategory() != null) {
            categoryRepository.findById(product.getCategory().getId())
                    .orElseThrow(EntityNotFoundException::new);
            //FIXME
        }
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
