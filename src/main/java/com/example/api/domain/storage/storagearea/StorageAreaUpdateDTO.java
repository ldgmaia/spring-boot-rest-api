package com.example.api.domain.storage.storagearea;

import com.example.api.domain.storage.storagelocation.StorageLocationUpdateDTO;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record StorageAreaUpdateDTO(
        @NotNull
        Long id,

        @NotNull
        String name,

        String description,

        @NotNull
        List<StorageLocationUpdateDTO> locations
) {
}
