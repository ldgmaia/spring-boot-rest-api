package com.example.api.domain.inventoryitems;

import java.time.LocalDateTime;

public record InventoryItemResponseDTO(
        Long id,
        String serialNumber,
        String companyGrade,
        String functionalGrade,
        String cosmeticGrade,
        LocalDateTime lastUpdated,
        String rbid,
        String mpn,
        String model,
        String itemCondition,
        String itemStatus,
        String location
) {
    public InventoryItemResponseDTO(InventoryItem inventoryItem) {
        this(
                inventoryItem.getId(),
                inventoryItem.getSerialNumber(),
                inventoryItem.getCompanyGrade(),
                inventoryItem.getFunctionalGrade(),
                inventoryItem.getCosmeticGrade(),
                inventoryItem.getUpdatedAt(),
                inventoryItem.getRbid(),
                inventoryItem.getMpn() != null ? inventoryItem.getMpn().getName() : null,
                inventoryItem.getModel().getName(),
                inventoryItem.getItemCondition().getName(),
                inventoryItem.getItemStatus().getName(),
                inventoryItem.getStorageLevel().getName()
        );
    }
}
