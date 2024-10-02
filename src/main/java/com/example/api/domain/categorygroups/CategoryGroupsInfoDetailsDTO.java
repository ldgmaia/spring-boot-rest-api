package com.example.api.domain.categorygroups;

import com.example.api.domain.categories.CategoryListDTO;

import java.util.List;

public record CategoryGroupsInfoDetailsDTO(
        Long id,
        String name,
        Boolean enabled,
        List<CategoryListDTO> categories
) {

}
