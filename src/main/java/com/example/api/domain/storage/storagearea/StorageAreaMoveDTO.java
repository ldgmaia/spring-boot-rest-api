package com.example.api.domain.storage.storagearea;

public record StorageAreaMoveDTO(
        Long areaId,
        Long newZoneId
) {
}
