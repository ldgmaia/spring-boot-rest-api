package com.example.api.domain.categories;

import com.example.api.domain.categoryfield.CategoryFieldsInfoDTO;

import java.util.List;

public record CategoryInfoDetailsDTO(
        Long id,
        String name,
        Boolean needsPost,
        Boolean needsSerialNumber,
        CategoryInfoDTO parentCategory,
        List<CategoryInfoDTO> components,
        List<CategoryFieldsInfoDTO> categoryFields


) {
}
