package com.example.api.domain.categoryfield;

import jakarta.validation.constraints.NotNull;

public record CategoryFieldsInfoDTO(

        @NotNull
        DataLevel dataLevel,

        @NotNull
        Long fieldId,

        Boolean printOnLabel,
        Boolean isMandatory
) {
}
