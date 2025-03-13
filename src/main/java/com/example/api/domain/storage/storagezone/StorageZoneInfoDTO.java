package com.example.api.domain.storage.storagezone;

import com.example.api.domain.storage.storagearea.StorageAreaInfoDTO;

import java.util.List;

public record StorageZoneInfoDTO(
        Long id,
        String name,
        String description,
        List<StorageAreaInfoDTO> areas
) {
    public StorageZoneInfoDTO(StorageZone data) {
        this(
                data.getId(),
                data.getName(),
                data.getDescription(),
                data.getAreas() != null
                        ? data.getAreas().stream()
                        .filter(area -> !area.getSystemManaged()) // Filter areas
                        .map(StorageAreaInfoDTO::new)
                        .toList()
                        : null
        );
    }
}
