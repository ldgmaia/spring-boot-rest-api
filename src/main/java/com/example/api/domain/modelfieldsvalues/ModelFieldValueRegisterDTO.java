package com.example.api.domain.modelfieldsvalues;

import com.example.api.domain.fieldsvalues.FieldValue;
import com.example.api.domain.models.Model;
import jakarta.validation.constraints.NotNull;

public record ModelFieldValueRegisterDTO(
        @NotNull
        FieldValue fieldValue,

        @NotNull
        Model model
) {
}
