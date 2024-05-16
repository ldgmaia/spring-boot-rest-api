package com.example.api.domain.categorygroups;

public record CategoryGroupInfoDTO(Long id, String name, Boolean enabled) {

    public CategoryGroupInfoDTO(CategoryGroup categoryGroup) {
        this(categoryGroup.getId(), categoryGroup.getName(), categoryGroup.getEnabled());
    }
}
