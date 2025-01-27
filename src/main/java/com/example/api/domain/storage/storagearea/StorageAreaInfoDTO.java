package com.example.api.domain.storage.storagearea;

import com.example.api.domain.storage.storagelocation.StorageLocationInfoDTO;

import java.util.List;

public record StorageAreaInfoDTO(
        Long id,
        String name,
        String description,
        List<StorageLocationInfoDTO> locations
) {
    public StorageAreaInfoDTO(StorageArea data) {
        this(
                data.getId(),
                data.getName(),
                data.getDescription(),
                data.getLocations().stream().map((StorageLocationInfoDTO::new)).toList()
        );
    }
}
