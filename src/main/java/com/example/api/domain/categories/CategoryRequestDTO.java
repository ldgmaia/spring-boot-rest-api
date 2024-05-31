package com.example.api.domain.categories;

import com.example.api.domain.categoryfield.CategoryFieldUpdateDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CategoryRequestDTO(
        @NotBlank
        String name,

        Long categoryGroupId,

        List<Long> parentCategory,

        @NotNull
        Boolean needsSerialNumber,

        @NotNull
        Boolean needsPost,

        @NotNull
        List<CategoryFieldUpdateDTO> fields
) {
}
