package com.example.api.domain.categories;

import com.example.api.domain.categorygroups.CategoryGroups;
import jakarta.validation.constraints.NotBlank;

public record CategoryRegisterDTO(
        @NotBlank
        String name,

        CategoryGroups categoryGroup,

//        Long categoryGroupId,
//        Long parentCategoryId,
        Boolean needsPost,
        Boolean needsSerialNumber


//        List<CategoryFieldRegisterDTO> fields
) {
}
