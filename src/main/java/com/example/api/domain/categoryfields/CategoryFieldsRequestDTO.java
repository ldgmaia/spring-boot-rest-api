package com.example.api.domain.categoryfields;

import jakarta.validation.constraints.NotNull;

public record CategoryFieldsRequestDTO(
        @NotNull
        DataLevel dataLevel,

        @NotNull
        Long fieldId,

        Boolean printOnLabel,
        Boolean isMandatory
) {
}
