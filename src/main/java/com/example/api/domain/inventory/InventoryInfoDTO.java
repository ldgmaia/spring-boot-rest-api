package com.example.api.domain.inventory;

public record InventoryInfoDTO(
        Long id,
        Long categoryId,
        Long modelId,
        Long mpnId,
        Long conditionId,
        Long receivingItemsId,
        Long createdBy,
        Boolean byQuantity,
        Long quantity,
        String post,
        String serialNumber

) {
    public InventoryInfoDTO(Inventory inventory) {
        this(
                inventory.getId(),
                inventory.getCategoryId(),
                inventory.getModelId(),
                inventory.getMpnId(),
                inventory.getConditionId(),
                inventory.getReceivingItemId(),
                inventory.getCreatedBy().getId(),
                inventory.getByQuantity(),
                inventory.getQuantity(),
                inventory.getPost(),
                inventory.getSerialNumber()
        );
    }
}