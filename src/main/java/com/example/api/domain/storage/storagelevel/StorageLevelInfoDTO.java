package com.example.api.domain.storage.storagelevel;

public record StorageLevelInfoDTO(
        Long id,
        String name,
        Long storageAreaId
) {
    public StorageLevelInfoDTO(StorageLevel data) {
        this(
                data.getId(),
                data.getName(),
                data.getStorageLocation().getStorageArea().getId()
        );
    }
}
