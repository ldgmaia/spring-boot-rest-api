package com.example.api.domain.fieldgroups;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record FieldGroupUpdateDTO(
        @NotNull
        Long id,
        String name,
        Boolean enabled,

        @Future
        LocalDateTime updatedAt
) {
}
