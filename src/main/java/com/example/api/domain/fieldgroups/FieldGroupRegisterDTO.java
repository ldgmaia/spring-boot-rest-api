package com.example.api.domain.fieldgroups;

import jakarta.validation.constraints.NotBlank;

public record FieldGroupRegisterDTO(
        @NotBlank
        String name
) {
}
