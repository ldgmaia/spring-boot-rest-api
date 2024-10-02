package com.example.api.domain.categories.validations;

import com.example.api.domain.ValidationException;
import com.example.api.domain.categories.CategoryRequestDTO;
import com.example.api.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DuplicateCategory implements CategoryValidator {

    @Autowired
    private CategoryRepository categoryRepository;

    public void validate(CategoryRequestDTO data) {

        var categoryNameFound = categoryRepository.existsByName(data.name());

        if (categoryNameFound) {
            throw new ValidationException("Category name already registered");
        }
    }
}
