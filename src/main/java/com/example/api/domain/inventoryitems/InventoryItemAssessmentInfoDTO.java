package com.example.api.domain.inventoryitems;

import com.example.api.domain.sections.SectionAssessmentInfoDTO;

import java.util.List;

public record InventoryItemAssessmentInfoDTO(
        Long id,
        String serialNumber,
        List<SectionAssessmentInfoDTO> sections
) {
    public InventoryItemAssessmentInfoDTO(InventoryItem inventoryItem) {
        this(
                inventoryItem.getId(),
                inventoryItem.getSerialNumber(),
                inventoryItem.getModel().getSections().stream().map(SectionAssessmentInfoDTO::new).toList()
        );
    }
}
