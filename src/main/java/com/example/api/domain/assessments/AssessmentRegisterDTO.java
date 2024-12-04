package com.example.api.domain.assessments;

import com.example.api.domain.inventoryitems.InventoryItem;
import com.example.api.domain.itemcondition.ItemCondition;
import com.example.api.domain.receivingitems.ReceivingItem;

public record AssessmentRegisterDTO(
        String dataLevel,
        String section,
        String area,
        Boolean present,
        String model,
        String mpn,
        Boolean pulled,
        String status,
        String post,
        String companyGrade,
        String cosmeticGrade,
        String functionalGrade,
        ItemCondition itemCondition,
        InventoryItem parentInventoryItem,
        InventoryItem inventoryItem,
        ReceivingItem receivingItem
) {

}
