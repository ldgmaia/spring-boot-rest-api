package com.example.api.domain.inventoryitemscomponents;

import com.example.api.domain.inventoryitems.InventoryItem;
import jakarta.validation.constraints.NotNull;

public record InventoryItemComponentRegisterDTO(
        InventoryItem parentInventoryItem,
        @NotNull InventoryItem inventoryItem
) {
}
