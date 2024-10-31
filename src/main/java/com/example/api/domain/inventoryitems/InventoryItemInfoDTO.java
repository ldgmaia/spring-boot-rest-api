package com.example.api.domain.inventoryitems;

import com.example.api.domain.itemcondition.ItemConditionInfoDTO;
import com.example.api.domain.itemstatus.ItemStatusInfoDTO;
import com.example.api.domain.locations.LocationInfoDTO;
import com.example.api.domain.models.ModelInfoDTO;
import com.example.api.domain.mpns.MPNInfoDTO;
import com.example.api.domain.users.UserInfoDTO;

import java.time.LocalDateTime;

public record InventoryItemInfoDTO(
        Long id,
        ModelInfoDTO model,
        MPNInfoDTO mpn,
        ItemConditionInfoDTO itemCondition,
        ItemStatusInfoDTO itemStatus,
//        ReceivingItemInfoDTO receivingItem,
        UserInfoDTO createdBy,
        UserInfoDTO inspectedBy,
        LocationInfoDTO location,
        String post,
        String serialNumber,
        String rbid,
        String cosmeticGrade,
        String companyGrade,
        String functionalGrade,
        LocalDateTime lastInspectedAt
) {
    public InventoryItemInfoDTO(InventoryItem inventoryItem) {
        this(
                inventoryItem.getId(),
                new ModelInfoDTO(inventoryItem.getModel()),
                new MPNInfoDTO(inventoryItem.getMpn()),
                new ItemConditionInfoDTO(inventoryItem.getItemCondition()),
                new ItemStatusInfoDTO(inventoryItem.getItemStatus()),
//                new ReceivingItemInfoDTO(inventoryItem.getReceivingItem()),
                new UserInfoDTO(inventoryItem.getCreatedBy()),
                inventoryItem.getInspectedBy() == null ? null : new UserInfoDTO(inventoryItem.getInspectedBy()),
                new LocationInfoDTO(inventoryItem.getLocation()),
                inventoryItem.getPost(),
                inventoryItem.getSerialNumber(),
                inventoryItem.getRbid(),
                inventoryItem.getCosmeticGrade(),
                inventoryItem.getCompanyGrade(),
                inventoryItem.getFunctionalGrade(),
                inventoryItem.getLastInspectedAt()
        );
    }
}
//[x]Category
//[x]Model
//[x]Serial Number
//[x]MPN
//[x]Status
//[x]Condition
//[x]Functional Grade --
//[ ]Assessed On -- need to get from assessment table
//[x]POST
//[x]Location
//[x]RBID
//[x]Last Inspected --
//[x]Inspected By --
//[x]Cosmetic Grade --
//[x]Company Grade  --
//[ ]Parent Item -- TBD
