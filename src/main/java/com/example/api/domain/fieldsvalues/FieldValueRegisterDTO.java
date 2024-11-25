package com.example.api.domain.fieldsvalues;

import com.example.api.domain.fields.Field;
import com.example.api.domain.values.Value;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FieldValueRegisterDTO(
        @NotBlank
        Value valueData,

        Double score,

        @NotNull
        Field field
) {
}
