package com.example.api.domain.mpns;

import com.example.api.domain.models.Model;
import com.example.api.domain.mpnfieldsvalues.MPNFieldValueRegisterDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record MPNRegisterDTO(
        @NotBlank
        String name,

        String description,
        String status,

        @NotNull
        Model model,

        List<MPNFieldValueRegisterDTO> mpnFieldsValues
) {
}
