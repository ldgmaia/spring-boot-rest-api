package com.example.api.domain.mpnfieldsvalues;

import jakarta.validation.constraints.NotNull;

public record MPNFieldValueRequestDTO(
        @NotNull
        Long fieldId,

        Long valueDataId,
        String valueData
) {
}
