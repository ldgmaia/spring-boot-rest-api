package com.example.api.domain.categories;

public record CategoryListDTO(Long id, String name, Boolean enabled, Boolean needsPost) {

    public CategoryListDTO(Category category) {
        this(category.getId(), category.getName(), category.getEnabled(), category.getNeedsPost());
    }
}
