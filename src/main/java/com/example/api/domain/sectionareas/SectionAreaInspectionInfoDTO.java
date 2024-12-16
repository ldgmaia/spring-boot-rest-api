package com.example.api.domain.sectionareas;

import com.example.api.domain.models.ModelInspectionInfoDTO;
import com.example.api.repositories.InventoryItemRepository;

import java.util.List;

public record SectionAreaInspectionInfoDTO(
        Long id,
        String name,
        Long areaOrder,
        Long currentModelId,
        List<ModelInspectionInfoDTO> models
) {
    public SectionAreaInspectionInfoDTO(SectionArea sectionArea, Long inventoryItemId, InventoryItemRepository inventoryItemRepository) {
        this(
                sectionArea.getId(),
                sectionArea.getName(),
                sectionArea.getAreaOrder(),
                inventoryItemRepository.findComponentModelIdBySectionAreaId(inventoryItemId, sectionArea.getId()).getModel().getId(),
                sectionArea.getSectionAreaModels().stream().map(sectionAreaModel -> new ModelInspectionInfoDTO(
                                sectionAreaModel.getModel(),
                                inventoryItemRepository.findComponentModelIdBySectionAreaId(inventoryItemId, sectionArea.getId()).getMpn() != null ?
                                        inventoryItemRepository.findComponentModelIdBySectionAreaId(inventoryItemId, sectionArea.getId()).getMpn().getId() : null
                        )
                ).toList()
        );
    }
}
