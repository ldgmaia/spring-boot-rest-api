package com.example.api.domain.fieldsvalues;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FieldValueRequestDTO(
        @NotBlank
        String valueData,

        Double score,

        @NotNull
        Long fieldId
) {
}
