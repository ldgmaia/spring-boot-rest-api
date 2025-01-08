package com.example.api.domain.inventoryitems.inspection;

import jakarta.validation.constraints.NotNull;

public record InventoryItemSaveInspectionFieldsRequestDTO(
        @NotNull
        Long fieldId,

        Long valueDataId,
        String valueData
) {
}
