package com.example.api.domain.sectionareas;

import com.example.api.domain.models.ModelInspectionInfoDTO;
import com.example.api.repositories.InventoryItemRepository;

import java.util.List;
import java.util.Optional;

public record SectionAreaInspectionInfoDTO(
        Long id,
        String name,
        Long areaOrder,
        Long currentModelId,
        String currentSerialNumber,
        Long inventoryItemId,
        Boolean present,
        List<ModelInspectionInfoDTO> models
) {
    public SectionAreaInspectionInfoDTO(SectionArea sectionArea, Long inventoryItemId, InventoryItemRepository inventoryItemRepository) {
        this(
                sectionArea.getId(),
                sectionArea.getName(),
                sectionArea.getAreaOrder(),
                getCurrentModelId(inventoryItemRepository, inventoryItemId, sectionArea.getId()),
                getCurrentSerialNumber(inventoryItemRepository, inventoryItemId, sectionArea.getId()),
                getCurrentInventoryItemId(inventoryItemRepository, inventoryItemId, sectionArea.getId()),
                getPresentStatus(inventoryItemRepository, inventoryItemId, sectionArea.getId()),
                sectionArea.getSectionAreaModels().stream()
                        .map(sectionAreaModel -> new ModelInspectionInfoDTO(
                                sectionAreaModel.getModel(),
                                getMpnId(inventoryItemRepository, inventoryItemId, sectionArea.getId()),
                                inventoryItemRepository,
                                inventoryItemId,
                                sectionArea
                        ))
                        .toList()
        );
    }

    private static Long getCurrentModelId(InventoryItemRepository repository, Long inventoryItemId, Long sectionAreaId) {
        return Optional.ofNullable(repository.findComponentModelIdBySectionAreaId(inventoryItemId, sectionAreaId))
                .map(component -> component.getModel().getId())
                .orElse(null);
    }

    private static Boolean getPresentStatus(InventoryItemRepository repository, Long inventoryItemId, Long sectionAreaId) {
        return Optional.ofNullable(repository.findComponentModelIdBySectionAreaId(inventoryItemId, sectionAreaId))
                .map(component -> component.getPresent())
                .orElse(false);
    }

    private static String getCurrentSerialNumber(InventoryItemRepository repository, Long inventoryItemId, Long sectionAreaId) {
        return Optional.ofNullable(repository.findComponentModelIdBySectionAreaId(inventoryItemId, sectionAreaId))
                .map(component -> component.getSerialNumber())
                .orElse(null);
    }

    private static Long getCurrentInventoryItemId(InventoryItemRepository repository, Long inventoryItemId, Long sectionAreaId) {
        return Optional.ofNullable(repository.findComponentModelIdBySectionAreaId(inventoryItemId, sectionAreaId))
                .map(component -> component.getId())
                .orElse(null);
    }

    private static Long getMpnId(InventoryItemRepository repository, Long inventoryItemId, Long sectionAreaId) {
        return Optional.ofNullable(repository.findComponentModelIdBySectionAreaId(inventoryItemId, sectionAreaId))
                .map(component -> Optional.ofNullable(component.getMpn())
                        .map(mpn -> mpn.getId())
                        .orElse(null))
                .orElse(null);
    }
}
