package com.example.api.domain.inventoryitems;

import com.example.api.domain.itemcondition.ItemConditionInfoDTO;
import com.example.api.domain.models.ModelInfoDTO;
import com.example.api.domain.mpns.MPNInfoDTO;
import com.example.api.domain.receivingitems.ReceivingItemInfoDTO;
import com.example.api.domain.users.UserInfoDTO;

public record InventoryInfoDTO(
        Long id,
//        CategoryInfoDTO category,
        ModelInfoDTO model,
        MPNInfoDTO mpn,
        ItemConditionInfoDTO itemCondition,
        ReceivingItemInfoDTO receivingItem,
        UserInfoDTO createdBy,
        String post,
        String serialNumber
) {
    public InventoryInfoDTO(InventoryItem inventoryItem) {
        this(
                inventoryItem.getId(),
//                new CategoryInfoDTO(inventoryItem.getCategory()),
                new ModelInfoDTO(inventoryItem.getModel()),
                new MPNInfoDTO(inventoryItem.getMpn()),
                new ItemConditionInfoDTO(inventoryItem.getItemCondition()),
                new ReceivingItemInfoDTO(inventoryItem.getReceivingItem()),
                new UserInfoDTO(inventoryItem.getCreatedBy()),
                inventoryItem.getPost(),
                inventoryItem.getSerialNumber()
        );
    }
}
