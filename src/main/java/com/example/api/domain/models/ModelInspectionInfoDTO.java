package com.example.api.domain.models;

import com.example.api.domain.categoryfields.CategoryFields;
import com.example.api.domain.categoryfields.CategoryFieldsInspectionInfoDTO;
import com.example.api.domain.mpns.MPNInfoDTO;
import com.example.api.repositories.InventoryItemRepository;

import java.util.List;

public record ModelInspectionInfoDTO(
        Long id,
        String name,
        Boolean needsSerialNumber,
        Boolean needsMpn,
        Boolean isParent,
        Long currentMpnId,
        List<MPNInfoDTO> mpns,
        List<CategoryFieldsInspectionInfoDTO> categoryFields
) {
    public ModelInspectionInfoDTO(Model model, Long currentMpnId, InventoryItemRepository inventoryItemRepository, Long inventoryItemId) {
        this(
                model.getId(),
                model.getName(),
                model.getCategory().getNeedsSerialNumber(),
                model.getNeedsMpn(),
                !model.getCategory().getParentCategory().isEmpty(),
                model.getNeedsMpn() != null ? currentMpnId : null,
                model.getMpns().stream().map(MPNInfoDTO::new).toList(),
                model.getCategory().getCategoryFields().stream()
                        .filter(CategoryFields::getEnabled)
                        .map(cf -> new CategoryFieldsInspectionInfoDTO(cf, inventoryItemRepository, inventoryItemId)).toList()
        );
    }
}
