package com.harlyn.service;

import com.harlyn.domain.problems.Category;
import com.harlyn.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by wannabe on 08.12.15.
 */
@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public Category findById(Long id) {
        return categoryRepository.findOne(id);
    }

    public Long createCategory(Category category) {
        return categoryRepository.saveAndFlush(category).getId();
    }

    public void updateCategory(Category category, Category categoryData) {
        if (categoryData.getName() != null) {
            category.setName(categoryData.getName());
        }
        categoryRepository.saveAndFlush(category);
    }

    public List<Category> findAll() {
        return categoryRepository.findAllByOrderByIdDesc();
    }

    public CategoryService setCategoryRepository(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
        return this;
    }
}
