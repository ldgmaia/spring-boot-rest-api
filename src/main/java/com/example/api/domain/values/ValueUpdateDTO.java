package com.example.api.domain.values;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ValueUpdateDTO(
        @NotNull
        Long id,
        String valueData,
        Boolean enabled,

        @Future
        LocalDateTime updatedAt
) {
}
