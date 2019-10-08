package com.amakedon.om.service;

import com.amakedon.om.data.exception.EntityNotFoundException;
import com.amakedon.om.data.model.Category;
import com.amakedon.om.data.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category findById(long id) {
        return categoryRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public void update(Category category) {
        categoryRepository.findById(category.getId())
                .orElseThrow(EntityNotFoundException::new);
        categoryRepository.save(category);
    }

    @Override
    public void deleteById(long id) {
        categoryRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
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
