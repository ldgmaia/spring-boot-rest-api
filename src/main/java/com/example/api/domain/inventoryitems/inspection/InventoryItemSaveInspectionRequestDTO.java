package com.example.api.domain.inventoryitems.inspection;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record InventoryItemSaveInspectionRequestDTO(
        @NotNull
        Long parentInventoryItemId,

        List<InventoryItemSaveInspectionFieldsRequestDTO> mainItemSpecs,
        List<InventoryItemSaveInspectionFieldsRequestDTO> mainItemFunctional,
        List<InventoryItemSaveInspectionFieldsRequestDTO> mainItemCosmetic,
        List<InventoryItemSaveInspectionAreaRequestDTO> specs,
        List<InventoryItemSaveInspectionAreaRequestDTO> functional,
        List<InventoryItemSaveInspectionAreaRequestDTO> cosmetic
) {
}
