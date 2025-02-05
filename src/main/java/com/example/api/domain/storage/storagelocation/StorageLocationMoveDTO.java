package com.example.api.domain.storage.storagelocation;

public record StorageLocationMoveDTO(
        Long locationId,
        Long newAreaId
) {
}
