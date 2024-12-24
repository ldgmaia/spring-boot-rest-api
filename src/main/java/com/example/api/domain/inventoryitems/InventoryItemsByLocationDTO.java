package com.example.api.domain.inventoryitems;

import com.example.api.domain.fieldsvalues.FieldValueInfoDTO;
import com.example.api.domain.models.ModelInfoDTO;

import java.util.List;

public record InventoryItemsByLocationDTO(
        Long id,
        String serialNumber,
        String rbid,
        ModelInfoDTO model,
        String mpn,
        String grade,
        List<FieldValueInfoDTO> fields
) {
    public InventoryItemsByLocationDTO(
            InventoryItem inventoryItem
    ) {
        this(
                inventoryItem.getId(),
                inventoryItem.getSerialNumber(),
                inventoryItem.getRbid(),
                new ModelInfoDTO(inventoryItem.getModel()),
                inventoryItem.getMpn() != null ? inventoryItem.getMpn().getName() : null,
                inventoryItem.getCompanyGrade() != null ? inventoryItem.getCompanyGrade() : null,
                inventoryItem.getInventoryItemsFieldsValues().stream().map(iifv -> new FieldValueInfoDTO(iifv.getFieldValue())).toList()
        );
    }
}
