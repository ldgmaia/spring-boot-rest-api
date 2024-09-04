package com.example.api.domain.files;

import jakarta.validation.constraints.NotBlank;

public record FileRequestDTO(
        @NotBlank
        String originalname,

        @NotBlank
        String filename
) {
}
