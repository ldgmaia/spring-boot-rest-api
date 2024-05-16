package com.example.api.domain.categorygroups;

import jakarta.validation.constraints.NotNull;

public record CategoryGroupUpdateDTO(
        @NotNull
        Long id,
        String name,
        Boolean enabled
) {
}
