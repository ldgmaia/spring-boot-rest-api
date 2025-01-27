package com.example.api.domain.storage.storagelocation;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record StorageLocationUpdateDTO(
        @NotNull
        Long id,

        @NotNull
        String name,

        String description,

        @NotNull
        List<String> levels
) {
}
