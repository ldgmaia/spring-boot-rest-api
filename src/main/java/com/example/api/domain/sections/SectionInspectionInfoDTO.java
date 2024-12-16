package com.example.api.domain.sections;

import com.example.api.domain.sectionareas.SectionAreaInspectionInfoDTO;
import com.example.api.repositories.InventoryItemRepository;

import java.util.List;

public record SectionInspectionInfoDTO(
        Long id,
        String name,
        Long sectionOrder,
        List<SectionAreaInspectionInfoDTO> areas
) {
    public SectionInspectionInfoDTO(Section section, Long inventoryItemId, InventoryItemRepository inventoryItemRepository) {
        this(
                section.getId(),
                section.getName(),
                section.getSectionOrder(),
                section.getAreas().stream().map(area -> new SectionAreaInspectionInfoDTO(area, inventoryItemId, inventoryItemRepository)).toList()
        );
    }
}
