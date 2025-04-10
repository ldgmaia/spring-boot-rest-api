package com.example.api.domain.inventoryitems;

import java.util.List;

public record InventoryItemLookUpComponentsResponseDTO(
        Long inventoryItemId,
        List<AreaDto> areas
) {
    public record AreaDto(
            Long id,
            String name,
            List<FieldDto> fields
    ) {
    }

    public record FieldDto(
            Long fieldId,
            String fieldName,
            Long valueDataId,
            String valueData
    ) {
    }
}
