package com.amakedon.om.service;

import com.amakedon.om.data.model.Category;

import java.util.List;

public interface CategoryService {

    Category findById(long id);

    Category save(Category category);

    void update(Category category);

    void deleteById(long id);

    List<Category> findAll();

    void deleteAll();

    boolean isCategoryExists(Category category);
}
