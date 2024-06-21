package com.example.api.domain.models;

import com.example.api.domain.categories.Category;
import jakarta.validation.constraints.NotBlank;

public record ModelRegisterDTO(

        @NotBlank
        String name,

        String description,
        String identifier,
        String status,
        Boolean needsMpn,

        @NotBlank
        Category category
) {
}
