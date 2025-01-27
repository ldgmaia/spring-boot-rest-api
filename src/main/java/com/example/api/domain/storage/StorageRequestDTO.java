package com.example.api.domain.storage;

import com.example.api.domain.storage.storagearea.StorageAreaRequestDTO;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record StorageRequestDTO(
        @NotNull
        String name,

        String description,

        @NotNull
        List<StorageAreaRequestDTO> areas
) {
}
