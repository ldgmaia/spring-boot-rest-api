package com.example.api.domain.categorygroups;

import com.example.api.domain.categories.CategoryListDTO;

import java.util.List;

public record CategoryGroupInfoDetailsDTO(
        Long id,
        String name,
        Boolean enabled,
        List<CategoryListDTO> categories
) {

}
