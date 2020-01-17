package com.amakedon.om.service;

import com.amakedon.om.exception.ResourceNotFoundException;
import com.amakedon.om.data.model.Category;
import com.amakedon.om.data.repository.jpa.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category findById(long id) {
        return categoryRepository
                .findById(id).orElseThrow(() -> new ResourceNotFoundException("Category with id " + id + "not found"));
    }

    @Override
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public void update(Category category) {
        findById(category.getId());
        categoryRepository.save(category);
    }

    @Override
    public void deleteById(long id) {
        findById(id);
        categoryRepository.deleteById(id);
    }

    @Override
    public List<Category> findAll() {
        List<Category> list = new ArrayList<>();
        categoryRepository.findAll().forEach(list::add);
        return list;
    }

    @Override
    public void deleteAll() {
        categoryRepository.deleteAll();
    }

    public boolean isCategoryExists(Category category) {
        return categoryRepository.findByName(category.getName()) != null;
    }
}
