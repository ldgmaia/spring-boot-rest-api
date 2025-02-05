package com.example.api.domain.storage.storagelevel;

public record StorageLevelMoveDTO(
        Long levelId,
        Long newLocationId
) {
}
