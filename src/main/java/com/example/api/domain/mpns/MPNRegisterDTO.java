package com.example.api.domain.mpns;

import com.example.api.domain.models.Model;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MPNRegisterDTO(
        @NotBlank
        String name,

        String description,
        String status,

        @NotNull
        Model model
) {
}
