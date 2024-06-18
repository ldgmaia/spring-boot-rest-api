package com.example.api.domain.categoryfield;

import com.example.api.domain.fields.DataType;
import com.example.api.domain.fields.FieldType;
import com.example.api.domain.values.ValueInfoDTO;

import java.util.List;

public record CategoryFieldsValuesInfoDTO(
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
        List<ValueInfoDTO> values
) {
    public CategoryFieldsValuesInfoDTO(CategoryField categoryField, List<ValueInfoDTO> values) {
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
                values
        );
    }
}
