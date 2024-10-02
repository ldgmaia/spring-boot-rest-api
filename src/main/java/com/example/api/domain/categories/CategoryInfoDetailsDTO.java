package com.example.api.domain.categories;

import com.example.api.domain.categoryfields.CategoryFieldsValuesInfoDTO;
import com.example.api.domain.categorygroups.CategoryGroupsInfoDTO;

import java.util.List;

public record CategoryInfoDetailsDTO(
        Long id,
        String name,
        Boolean needsPost,
        Boolean needsSerialNumber,
        CategoryGroupsInfoDTO categoryGroup,
        List<CategoryInfoDTO> parentCategory,
        List<CategoryInfoDTO> components,
        List<CategoryFieldsValuesInfoDTO> categoryFieldsValues
) {
}
