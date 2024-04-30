package com.example.api.domain.fields;

import com.example.api.domain.fieldgroups.FieldGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FieldRegisterDTO(
        @NotBlank
        String name,

        Boolean isMultiple,

        @NotNull
        DataType dataType,

        @NotNull
        FieldType fieldType,

        FieldGroup fieldGroup
) {
}
