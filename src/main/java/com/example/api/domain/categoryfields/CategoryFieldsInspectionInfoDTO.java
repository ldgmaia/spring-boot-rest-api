package com.example.api.domain.categoryfields;

import com.example.api.domain.fields.DataType;
import com.example.api.domain.fields.FieldType;
import com.example.api.domain.sectionareas.SectionArea;
import com.example.api.domain.values.ValueInfoDTO;
import com.example.api.repositories.InventoryItemRepository;

import java.util.List;

public record CategoryFieldsInspectionInfoDTO(
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
    public CategoryFieldsInspectionInfoDTO(CategoryFields categoryField) {
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
                categoryField.getEnabled(),
                null,
                null,
                categoryField.getField().getFieldValues().stream().map(fieldValue -> new ValueInfoDTO(fieldValue.getValueData())).toList()
        );
    }

    public CategoryFieldsInspectionInfoDTO(CategoryFields categoryField, InventoryItemRepository inventoryItemRepository, Long inventoryItemId, SectionArea sectionArea) {
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
                categoryField.getEnabled(),
                inventoryItemRepository.findValuesDataBySectionAreaAndFieldIdOfComponent(inventoryItemId, sectionArea, categoryField.getField().getId()) != null ?
                        inventoryItemRepository.findValuesDataBySectionAreaAndFieldIdOfComponent(inventoryItemId, sectionArea, categoryField.getField().getId()).getId() :
                        null,
                inventoryItemRepository.findValuesDataBySectionAreaAndFieldIdOfComponent(inventoryItemId, sectionArea, categoryField.getField().getId()) != null ?
                        inventoryItemRepository.findValuesDataBySectionAreaAndFieldIdOfComponent(inventoryItemId, sectionArea, categoryField.getField().getId()).getValueData() :
                        null,
                categoryField.getField().getFieldValues().stream().map(fieldValue -> new ValueInfoDTO(fieldValue.getValueData())).toList()
        );
    }

//    public CategoryFieldsInspectionInfoDTO(CategoryFields categoryField, InventoryItemRepository inventoryItemRepository, Long inventoryItemId) {
//        this(
//                categoryField.getId(),
//                categoryField.getDataLevel(),
//                categoryField.getIsMandatory(),
//                categoryField.getPrintOnLabel(),
//                categoryField.getField().getId(),
//                categoryField.getField().getName(),
//                categoryField.getField().getDataType(),
//                categoryField.getField().getFieldType(),
//                categoryField.getField().getIsMultiple(),
//                categoryField.getEnabled(),
//                inventoryItemRepository.findComponentFieldValueDataIdByInventoryItemIdAndFieldIdAndCategoryId(inventoryItemId, categoryField.getField().getId(), categoryField.getCategory().getId()) != null ?
//                        inventoryItemRepository.findComponentFieldValueDataIdByInventoryItemIdAndFieldIdAndCategoryId(inventoryItemId, categoryField.getField().getId(), categoryField.getCategory().getId()).getId() :
//                        null,
//                inventoryItemRepository.findComponentFieldValueDataIdByInventoryItemIdAndFieldIdAndCategoryId(inventoryItemId, categoryField.getField().getId(), categoryField.getCategory().getId()) != null ?
//                        inventoryItemRepository.findComponentFieldValueDataIdByInventoryItemIdAndFieldIdAndCategoryId(inventoryItemId, categoryField.getField().getId(), categoryField.getCategory().getId()).getValueData() :
//                        null,
//                categoryField.getField().getFieldValues().stream().map(fieldValue -> new ValueInfoDTO(fieldValue.getValueData())).toList()
//        );
//    }
}
