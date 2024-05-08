package com.example.api.domain.fieldsvalues;

import com.example.api.domain.fields.Field;
import com.example.api.domain.values.Value;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// id
// values_data_id
// fields_id
// score
// enabled
// created_at
// updated_at
public record FieldValueRegisterDTO(
        @NotBlank
        Value valueData,

        Double score,

        @NotNull
        Field field
) {
}
