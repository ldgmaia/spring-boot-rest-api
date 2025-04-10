package com.example.api.domain.inventoryitems;

import java.util.List;

public record InventoryItemLookUpResponseDTO(
        Long inventoryItemId,
        String serialNumber,
        String Mpn,
        String Grade,
        String Location,
        String Status,
        List<InventoryItemLookUpComponentsResponseDTO.FieldDto> fields
) {
    public InventoryItemLookUpResponseDTO(InventoryItem inventoryItem) {
        this(
                inventoryItem.getId(),
                inventoryItem.getSerialNumber(),
                inventoryItem.getMpn().getName(),
                inventoryItem.getCompanyGrade(),
                inventoryItem.getStorageLevel().getName(),
                inventoryItem.getItemStatus().getName(),
                inventoryItem.getInventoryItemsFieldsValues().stream().map(iifv -> new InventoryItemLookUpComponentsResponseDTO.FieldDto(
                        iifv.getFieldValue().getField().getId(),
                        iifv.getFieldValue().getField().getName(),
                        iifv.getFieldValue().getId(),
                        iifv.getFieldValue().getValueData().getValueData()
                )).toList()
        );
    }
}
