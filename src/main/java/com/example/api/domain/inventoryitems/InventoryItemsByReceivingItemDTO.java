package com.example.api.domain.inventoryitems;

import java.time.LocalDateTime;

public record InventoryItemsByReceivingItemDTO(
        Long id,
        String serialNumber,
        String grade,
        LocalDateTime lastUpdated,
        String rbid,
        String post,
        String mpn,
        String model,
        String itemCondition,
        String itemStatus,
        String location
) {
    public InventoryItemsByReceivingItemDTO(
            InventoryItem inventoryItem,
            String mpn,
            String model,
            String itemCondition,
            String itemStatus,
            String location
    ) {
        this(
                inventoryItem.getId(),
                inventoryItem.getSerialNumber(),
                inventoryItem.getCompanyGrade(),
                inventoryItem.getUpdatedAt(),
                inventoryItem.getRbid(),
                inventoryItem.getPost(),
                mpn,
                model,
                itemCondition,
                itemStatus,
                location
        );
    }
}
