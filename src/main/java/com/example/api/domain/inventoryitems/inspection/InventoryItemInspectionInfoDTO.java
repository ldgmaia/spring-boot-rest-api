package com.example.api.domain.inventoryitems.inspection;

import com.example.api.domain.inventoryitems.InventoryItem;
import com.example.api.domain.sections.SectionInspectionInfoDTO;
import com.example.api.repositories.InventoryItemRepository;

import java.util.List;

public record InventoryItemInspectionInfoDTO(
        Long id,
        String serialNumber,
        List<SectionInspectionInfoDTO> sections
) {
    public InventoryItemInspectionInfoDTO(InventoryItem inventoryItem, InventoryItemRepository inventoryItemRepository) {
        this(
                inventoryItem.getId(),
                inventoryItem.getSerialNumber(),
                inventoryItem.getModel().getSections().stream()
                        .sorted((a, b) -> a.getSectionOrder().compareTo(b.getSectionOrder()))
                        .map(section -> new SectionInspectionInfoDTO(section, inventoryItem.getId(), inventoryItemRepository))
                        .toList()
        );
    }
}
