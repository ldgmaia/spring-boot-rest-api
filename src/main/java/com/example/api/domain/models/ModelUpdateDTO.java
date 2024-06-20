package com.example.api.domain.models;

import com.example.api.domain.modelfieldsvalues.ModelFieldValueRequestDTO;
import com.example.api.domain.sections.SectionUpdateDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ModelUpdateDTO(

        @NotBlank
        String name,

        String description,
        String identifier,

        @NotNull
        Long categoryId,

        Boolean needsMpn,

        List<ModelFieldValueRequestDTO> modelFieldsValues,

        List<SectionUpdateDTO> sections
) {
}
