package com.example.api.domain.storage.storagearea;

import com.example.api.domain.storage.storagelocation.StorageLocationRequestDTO;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record StorageAreaRequestDTO(
        @NotNull
        String name,

        String description,

        @NotNull
        List<StorageLocationRequestDTO> locations
) {
}
