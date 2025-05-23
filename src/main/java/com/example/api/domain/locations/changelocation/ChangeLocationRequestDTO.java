package com.example.api.domain.locations.changelocation;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ChangeLocationRequestDTO(
        @NotNull
        Long toLocationLevelId,

        @NotNull
        List<Long> ids
) {
}
