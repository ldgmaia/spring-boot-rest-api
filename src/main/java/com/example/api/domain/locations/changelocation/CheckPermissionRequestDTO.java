package com.example.api.domain.locations.changelocation;

import jakarta.validation.constraints.NotNull;

public record CheckPermissionRequestDTO(
        @NotNull
        Long fromLocationAreaId,

        @NotNull
        Long toLocationAreaId
) {
}
