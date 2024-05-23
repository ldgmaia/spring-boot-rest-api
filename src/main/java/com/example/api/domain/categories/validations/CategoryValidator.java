package com.example.api.domain.categories.validations;

import com.example.api.domain.categories.CategoryRequestDTO;

public interface CategoryValidator {

    void validate(CategoryRequestDTO data);
}
