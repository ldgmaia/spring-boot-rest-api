package com.example.api.domain.settings.qbo;

import jakarta.validation.constraints.NotBlank;

public record QboCallbackRequestDTO(
        @NotBlank
        String code,

        @NotBlank
        String state,
        
        @NotBlank
        String realmId
) {
}
