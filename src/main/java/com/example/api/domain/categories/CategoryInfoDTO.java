package com.example.api.domain.categories;

import com.example.api.domain.categorygroups.CategoryGroupInfoDTO;

public record CategoryInfoDTO(
        Long id,
        String name,
        CategoryGroupInfoDTO categoryGroup,
        Boolean enabled,
        Boolean needsSerialNumber,
        Boolean needsPost) {


    public CategoryInfoDTO(Category category) {
        this(
                category.getId(),
                category.getName(),
                new CategoryGroupInfoDTO(category.getCategoryGroup()),
                category.getEnabled(),
                category.getNeeds_serial_number(),
                category.getNeeds_post()
        );
    }
}
