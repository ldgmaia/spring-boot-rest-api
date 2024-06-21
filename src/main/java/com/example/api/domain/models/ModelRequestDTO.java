package com.example.api.domain.models;

import com.example.api.domain.modelfieldsvalues.ModelFieldValueRequestDTO;
import com.example.api.domain.sections.SectionRequestDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ModelRequestDTO(

        @NotBlank
        String name,

        String description,
        String identifier,
        String status,

        @NotNull
        Long categoryId,

        Boolean needsMpn,

        List<ModelFieldValueRequestDTO> modelFieldsValues,

        List<SectionRequestDTO> sections
) {
}
