package com.example.api.domain.categorygroups;

public record CategoryGroupsInfoDTO(Long id, String name, Boolean enabled) {

    public CategoryGroupsInfoDTO(CategoryGroups categoryGroup) {
        this(categoryGroup.getId(), categoryGroup.getName(), categoryGroup.getEnabled());
    }
}
