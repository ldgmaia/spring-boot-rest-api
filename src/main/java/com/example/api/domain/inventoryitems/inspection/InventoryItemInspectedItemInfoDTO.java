package com.example.api.domain.inventoryitems.inspection;

import com.example.api.domain.fieldsvalues.FieldValueInfoDTO;
import com.example.api.domain.inventoryitems.InventoryItem;
import com.example.api.domain.models.ModelInfoDTO;
import com.example.api.domain.mpns.MPNInfoDTO;

import java.util.List;

public record InventoryItemInspectedItemInfoDTO(
        Long id,
        String serialNumber,
        String rbid,
        ModelInfoDTO model,
        MPNInfoDTO mpn,
        List<FieldValueInfoDTO> fields
) {
    public InventoryItemInspectedItemInfoDTO(InventoryItem inventoryItem) {
        this(
                inventoryItem.getId(),
                inventoryItem.getSerialNumber(),
                inventoryItem.getRbid(),
                new ModelInfoDTO(inventoryItem.getModel()),
                inventoryItem.getMpn() != null ? new MPNInfoDTO(inventoryItem.getMpn()) : null,
                inventoryItem.getInventoryItemsFieldsValues() != null ? inventoryItem.getInventoryItemsFieldsValues().stream().map(iifv -> new FieldValueInfoDTO(iifv.getFieldValue())).toList() : null
        );
    }
}
