package com.example.api.domain.inventoryitems.inspection;

import java.util.List;

public record InventoryItemSaveInspectionAreaRequestDTO(
        Boolean present,
        String section,
        String area,
        Long areaId,
        Long modelId,
        Long mpnId,
        Long inventoryItemId,
        Boolean needsMpn,
        Boolean needsSerialNumber,
        List<InventoryItemSaveInspectionFieldsRequestDTO> fields
) {
}
