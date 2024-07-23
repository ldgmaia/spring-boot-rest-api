package com.example.api.domain.fields;

import jakarta.validation.constraints.NotBlank;

public record FieldUpdateDTO(
//        @NotNull
//        Long id,

        @NotBlank
        String name,
//
//        Boolean isMultiple,
//
//        @NotNull
//        FieldType fieldType,

        Long fieldGroupId
) {
}
