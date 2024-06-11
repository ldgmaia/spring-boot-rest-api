package com.example.api.domain.categories;

import com.example.api.domain.categoryfield.CategoryFieldsInfoDTO;
import com.example.api.domain.categorygroups.CategoryGroupInfoDTO;

import java.util.List;

public record CategoryInfoDetailsDTO(
        Long id,
        String name,
        Boolean needsPost,
        Boolean needsSerialNumber,
        CategoryGroupInfoDTO categoryGroup,
        List<CategoryInfoDTO> parentCategory,
        List<CategoryInfoDTO> components,
        List<CategoryFieldsInfoDTO> categoryFields
) {
}
