package com.example.api.domain.fields;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FieldUpdateDTO(
        @NotNull
        Long id,

        @NotBlank
        String name,

        Boolean isMultiple,

        @NotNull
        DataType dataType,

        @NotNull
        FieldType fieldType,

        Long fieldGroupId
) {
}
