package com.example.api.domain.sectionareamodels;

import com.example.api.domain.models.Model;
import com.example.api.domain.sectionareas.SectionArea;
import jakarta.validation.constraints.NotNull;

public record SectionAreaModelRegisterDTO(
        @NotNull
        SectionArea sectionArea,

        @NotNull
        Model model
) {
}
