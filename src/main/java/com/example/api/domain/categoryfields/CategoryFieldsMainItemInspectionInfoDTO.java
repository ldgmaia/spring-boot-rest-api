package com.example.api.domain.categoryfields;

import com.example.api.domain.fields.DataType;
import com.example.api.domain.fields.FieldType;
import com.example.api.domain.values.ValueInfoDTO;
import com.example.api.repositories.InventoryItemRepository;

import java.util.List;

public record CategoryFieldsMainItemInspectionInfoDTO(
        Long id,
        DataLevel dataLevel,
        Boolean isMandatory,
        Boolean printOnLabel,
        Long fieldId,
        String name,
        DataType dataType,
        FieldType fieldType,
        Boolean isMultiple,
        Boolean enabled,
        Long currentValueDataId,
        String currentValueData,
        List<ValueInfoDTO> values
) {
    public CategoryFieldsMainItemInspectionInfoDTO(CategoryFields categoryField, InventoryItemRepository inventoryItemRepository, Long inventoryItemId) {
        this(
                categoryField.getId(),
                categoryField.getDataLevel(),
                categoryField.getIsMandatory(),
                categoryField.getPrintOnLabel(),
                categoryField.getField().getId(),
                categoryField.getField().getName(),
                categoryField.getField().getDataType(),
                categoryField.getField().getFieldType(),
                categoryField.getField().getIsMultiple(),
                categoryField.getField().getEnabled(),
                inventoryItemRepository.findMainItemFieldValueDataIdByInventoryItemId(inventoryItemId, categoryField.getField().getId()) != null ?
                        inventoryItemRepository.findMainItemFieldValueDataIdByInventoryItemId(inventoryItemId, categoryField.getField().getId()).getId() :
                        null,
                inventoryItemRepository.findMainItemFieldValueDataIdByInventoryItemId(inventoryItemId, categoryField.getField().getId()) != null ?
                        inventoryItemRepository.findMainItemFieldValueDataIdByInventoryItemId(inventoryItemId, categoryField.getField().getId()).getValueData() :
                        null,
                categoryField.getField().getFieldValues().stream().map(fieldValue -> new ValueInfoDTO(fieldValue.getValueData())).toList()
        );
    }
}
