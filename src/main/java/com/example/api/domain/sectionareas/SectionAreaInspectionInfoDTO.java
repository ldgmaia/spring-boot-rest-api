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
                                inventoryItemId
                        ))
                        .toList()
        );
    }

//    private static Long getCurrentModelId(InventoryItemRepository repository, Long inventoryItemId, Long sectionAreaId) {
//        List<InventoryItem> components = repository.findComponentsBySectionAreaId(inventoryItemId, sectionAreaId);
//
//        if (components.isEmpty()) {
//            System.out.println("No results found.");
//            return null;
//        }
//
//        components.forEach(comp -> {
////            System.out.println(comp.getSerialNumber());
////            System.out.println(comp.getModel().getName());
//            System.out.println(inventoryItemId);
//            System.out.println(comp.getSectionArea().getId());
//            System.out.println(comp.getId());
//        });
//
//        System.out.println("Number of results: " + components.size());

    /// /        components.forEach(component -> System.out.println("Component ID: " + component.getId() + ", Model ID: " + component.getModel().getId() + ", section size: " + component.getModel().getSections().size()));
//
//        if (components.size() > 1) {
//            throw new RuntimeException("Query returned multiple results when one was expected.");
//        }
//
//        return components.get(0).getModel().getId();
//    }
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
