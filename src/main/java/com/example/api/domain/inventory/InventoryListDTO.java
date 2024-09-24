package com.example.api.domain.inventory;

public record InventoryListDTO(
        Inventory id,
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

    // Custom constructor must call the canonical constructor
    public InventoryListDTO(Inventory inventory) {
        this(
                inventory,
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
