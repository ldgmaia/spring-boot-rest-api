package com.example.api.domain.storage.storagelocation;

import com.example.api.domain.storage.storagelevel.StorageLevelRequestDTO;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record StorageLocationRequestDTO(
        @NotNull
        String name,

        String description,

        @NotNull
        List<StorageLevelRequestDTO> levels
) {
}
