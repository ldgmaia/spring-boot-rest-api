package com.example.api.domain.storage.storagelocation;

import com.example.api.domain.storage.storagelevel.StorageLevelInfoDTO;

import java.util.List;

public record StorageLocationInfoDTO(
        Long id,
        String name,
        String description,
        List<StorageLevelInfoDTO> levels
) {
    public StorageLocationInfoDTO(StorageLocation data) {
        this(
                data.getId(),
                data.getName(),
                data.getDescription(),
                data.getLevels().stream().map(StorageLevelInfoDTO::new).toList()
        );
    }
}
