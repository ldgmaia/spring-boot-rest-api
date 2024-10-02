package com.example.api.domain.categorygroups;

import jakarta.validation.constraints.NotBlank;

public record CategoryGroupsRegisterDTO(
        @NotBlank
        String name
) {
}
