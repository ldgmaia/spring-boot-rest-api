package com.example.api.domain.mpns;

import com.example.api.domain.mpnfieldsvalues.MPNFieldValueRequestDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record MPNRequestDTO(
        @NotBlank
        String name,

        String description,
        String status,

        @NotNull
        Long modelId,

        List<MPNFieldValueRequestDTO> mpnFieldsValues
) {
}
