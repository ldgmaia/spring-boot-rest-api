//package com.example.api.domain.inventoryitems;
//
//public record InventoryInfoDTO(
//        Long id,
//        Long categoryId,
//        Long modelId,
//        Long mpnId,
//        Long itemConditionsId,
//        Long receivingItemsId,
//        Long createdBy,
//        String post,
//        String serialNumber
//) {
//    public InventoryInfoDTO(Inventory inventory) {
//        this(
//                inventory.getId(),
//                inventory.getCategoryId(),
//                inventory.getModelId(),
//                inventory.getMpnId(),
//                inventory.getItemConditionsId(),
//                inventory.getReceivingItemId(),
//                inventory.getCreatedBy().getId(),
//                inventory.getPost(),
//                inventory.getSerialNumber()
//        );
//    }
//}
