package com.example.api.domain.storage;

import com.example.api.domain.storage.storagearea.StorageAreaUpdateDTO;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record StorageUpdateDTO(
        @NotNull
        Long id,

        @NotNull
        String name,

        String description,

        @NotNull
        List<StorageAreaUpdateDTO> areas
) {
}
