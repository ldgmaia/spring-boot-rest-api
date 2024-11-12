package com.example.api.domain.categoryfields;

import com.example.api.domain.fields.DataType;
import com.example.api.domain.fields.FieldType;

public record CategoryFieldsAssessmentInfoDTO(
        Long id,
        DataLevel dataLevel,
        Boolean isMandatory,
        Boolean printOnLabel,
        Long fieldId,
        String name,
        DataType dataType,
        FieldType fieldType,
        Boolean isMultiple,
        Boolean enabled
) {
    public CategoryFieldsAssessmentInfoDTO(CategoryFields categoryField) {
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
                categoryField.getField().getEnabled()
        );
    }
}
