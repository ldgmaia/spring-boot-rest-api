package com.example.api.domain.inventoryitems;

import com.example.api.domain.categories.Category;
import com.example.api.domain.itemcondition.ItemCondition;
import com.example.api.domain.itemstatus.ItemStatus;
import com.example.api.domain.models.Model;
import com.example.api.domain.mpns.MPN;
import com.example.api.domain.receivingitems.ReceivingItem;
import com.example.api.domain.sectionareas.SectionArea;
import com.example.api.domain.storage.storagelevel.StorageLevel;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record InventoryItemRegisterDTO(
        @NotNull Category category,
        @NotNull Model model,
        MPN mpn,
        @NotNull ItemCondition itemCondition,
        @NotNull ItemStatus itemStatus,
        @NotNull ReceivingItem receivingItem,
        @NotNull StorageLevel storageLevel,
        String post,
        Boolean present,
        SectionArea sectionArea,
        String serialNumber,
        String rbid,
        String type,
        BigDecimal cost,
        String companyGrade
) {
}
