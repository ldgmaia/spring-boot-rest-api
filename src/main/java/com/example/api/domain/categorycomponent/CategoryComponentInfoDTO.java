package com.example.api.domain.categorycomponent;

import com.example.api.domain.categories.Category;

public record CategoryComponentInfoDTO(Long id, String name) {
    public CategoryComponentInfoDTO(Category parentCategory) {
        this(parentCategory.getId(), parentCategory.getName());
    }
}
