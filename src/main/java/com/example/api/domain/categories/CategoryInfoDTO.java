package com.example.api.domain.categories;

import com.example.api.domain.categorygroups.CategoryGroupsInfoDTO;

public record CategoryInfoDTO(
        Long id,
        String name,
        CategoryGroupsInfoDTO categoryGroup,
        Boolean enabled,
        Boolean needsSerialNumber,
        Boolean needsPost
) {
    public CategoryInfoDTO(Category category) {
        this(
                category.getId(),
                category.getName(),
                new CategoryGroupsInfoDTO(category.getCategoryGroup()),
                category.getEnabled(),
                category.getNeedsSerialNumber(),
                category.getNeedsPost()
        );
    }
}
