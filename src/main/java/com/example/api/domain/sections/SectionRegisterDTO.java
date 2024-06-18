package com.example.api.domain.sections;

import com.example.api.domain.models.Model;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SectionRegisterDTO(
        @NotBlank
        String name,

        Long sectionOrder,

        @NotNull
        Model model
) {
}
