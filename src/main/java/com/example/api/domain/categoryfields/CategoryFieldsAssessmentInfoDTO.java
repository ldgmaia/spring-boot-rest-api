package com.example.api.domain.categoryfields;

import com.example.api.domain.fields.DataType;
import com.example.api.domain.fields.FieldType;
import com.example.api.domain.models.Model;
import com.example.api.domain.values.ValueInfoDTO;

import java.util.List;

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
        Boolean enabled,
        Boolean modelNeedsMpn,
        List<ValueInfoDTO> values
) {
    public CategoryFieldsAssessmentInfoDTO(CategoryFields categoryField, Model model) {
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
                model.getNeedsMpn(),
                categoryField.getField().getFieldValues().stream().map(fieldValue -> new ValueInfoDTO(fieldValue.getValueData(), fieldValue.getScore())).toList()
        );
    }
}
