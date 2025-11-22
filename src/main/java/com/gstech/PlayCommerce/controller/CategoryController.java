package com.gstech.PlayCommerce.controller;

import com.gstech.PlayCommerce.dto.CategoryRequestDTO;
import com.gstech.PlayCommerce.model.Category;
import com.gstech.PlayCommerce.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody CategoryRequestDTO request) {
        Category category = new Category();
        category.setName(request.name());
        return ResponseEntity.ok(categoryRepository.save(category));
    }
}
