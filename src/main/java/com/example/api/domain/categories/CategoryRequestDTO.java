package com.example.api.domain.categories;

import com.example.api.domain.categoryfield.CategoryFieldRequestDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CategoryRequestDTO(
        @NotBlank
        String name,

        Long categoryGroupId,
        Long parentCategoryId,

        @NotNull
        Boolean needsSerialNumber,

        @NotNull
        Boolean needsPost,

        @NotNull
        List<CategoryFieldRequestDTO> fields
) {
}
