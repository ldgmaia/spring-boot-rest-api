package com.example.api.domain.categoryfield;

import com.example.api.domain.categories.DataLevel;
import jakarta.validation.constraints.NotNull;

public record CategoryFieldsDTO(

        @NotNull
        DataLevel dataLevel,

        @NotNull
        Long fieldId,

        @NotNull
        Boolean printOnLabel,

        @NotNull
        Boolean isMandatory
) {
}
