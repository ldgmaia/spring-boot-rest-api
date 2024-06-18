package com.example.api.domain.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ModelRequestDTO(

        @NotBlank
        String name,

        String description,
        String identifier,

        @NotNull
        Long categoryId,

        Boolean needsMpn
) {
}
