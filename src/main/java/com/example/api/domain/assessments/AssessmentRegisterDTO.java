package com.example.api.domain.assessments;

import com.example.api.domain.inventoryitems.InventoryItem;
import com.example.api.domain.itemcondition.ItemCondition;
import com.example.api.domain.locations.Location;
import com.example.api.domain.models.Model;
import com.example.api.domain.mpns.MPN;
import com.example.api.domain.receivingitems.ReceivingItem;
import com.example.api.domain.sectionareas.SectionArea;
import com.example.api.domain.users.User;

public record AssessmentRegisterDTO(
        Boolean pulled,
        Boolean present,
        String status,
        String companyGrade,
        String cosmeticGrade,
        String functionalGrade,
        SectionArea sectionArea,
        InventoryItem parentInventoryItem,
        InventoryItem inventoryItem
) {

}
