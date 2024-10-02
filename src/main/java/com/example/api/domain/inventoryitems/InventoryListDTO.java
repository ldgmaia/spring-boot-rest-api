package com.example.api.domain.inventoryitems;

public record InventoryListDTO(
        Inventory id,
        Long categoryId,
        Long modelId,
        Long mpnId,
        Long itemConditionsId,
        Long receivingItemsId,
        Long createdBy,
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
                inventory.getItemConditionsId(),
                inventory.getReceivingItemId(),
                inventory.getCreatedBy().getId(),
                inventory.getPost(),
                inventory.getSerialNumber()
        );
    }

}
