package com.example.api.domain.values;

import jakarta.validation.constraints.NotBlank;

public record ValueRegisterDTO(
        @NotBlank
        String valueData
) {
}
