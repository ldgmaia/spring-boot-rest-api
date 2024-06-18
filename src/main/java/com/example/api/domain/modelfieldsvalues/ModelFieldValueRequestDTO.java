package com.example.api.domain.modelfieldsvalues;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ModelFieldValueRequestDTO(
        @NotBlank
        Long valueDataId,

        @NotNull
        Long fieldId
) {
}
