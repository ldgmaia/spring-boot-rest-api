package com.example.api.domain.assessments;

import com.example.api.domain.inventoryitems.InventoryItem;
import com.example.api.domain.receivingitems.ReceivingItem;
import com.example.api.domain.sectionareas.SectionArea;

public record AssessmentRegisterDTO(
        Boolean pulled,
        Boolean present,
        String status,
        String companyGrade,
        String cosmeticGrade,
        String functionalGrade,
        SectionArea sectionArea,
        InventoryItem parentInventoryItem,
        InventoryItem inventoryItem,
        ReceivingItem receivingItem
) {

}
