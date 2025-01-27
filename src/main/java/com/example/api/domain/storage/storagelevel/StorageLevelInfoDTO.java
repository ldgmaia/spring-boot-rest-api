package com.example.api.domain.storage.storagelevel;

public record StorageLevelInfoDTO(
        Long id,
        String name
) {
    public StorageLevelInfoDTO(StorageLevel data) {
        this(
                data.getId(),
                data.getName()
        );
    }
}
