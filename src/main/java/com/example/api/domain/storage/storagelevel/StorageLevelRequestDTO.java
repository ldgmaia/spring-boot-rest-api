package com.example.api.domain.storage.storagelevel;

import jakarta.validation.constraints.NotNull;

public record StorageLevelRequestDTO(
        Long id,

        @NotNull
        String name
) {
}
