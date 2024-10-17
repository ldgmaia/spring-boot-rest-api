//package com.example.api.domain.inventoryitems;
//
//import com.example.api.domain.categories.Category;
//import com.example.api.domain.itemcondition.ItemCondition;
//import com.example.api.domain.models.Model;
//import com.example.api.domain.mpns.MPN;
//import com.example.api.domain.receivingitems.ReceivingItem;
//
//public record InventoryItemListDTO(
//        InventoryItem id,
//        Category category,
//        Model model,
//        MPN mpn,
//        ItemCondition itemConditions,
//        ReceivingItem receivingItems,
//        Long createdBy,
//        String post,
//        String serialNumber
//) {
//
//    // Custom constructor must call the canonical constructor
//    public InventoryItemListDTO(InventoryItem inventory) {
//        this(
//                inventory,
//                inventory.getCategory(),
//                inventory.getModel(),
//                inventory.getMpn(),
//                inventory.getItemCondition(),
//                inventory.getReceivingItem(),
//                inventory.getCreatedBy().getId(),
//                inventory.getPost(),
//                inventory.getSerialNumber()
//        );
//    }
//
//}
