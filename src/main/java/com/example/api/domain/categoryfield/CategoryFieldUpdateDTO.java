package com.example.api.domain.categoryfield;

import jakarta.validation.constraints.NotNull;

public record CategoryFieldUpdateDTO(
        @NotNull
        Long id,
        
        @NotNull
        DataLevel dataLevel,

        @NotNull
        Long fieldId,

        Boolean printOnLabel,
        Boolean isMandatory
) {
}
