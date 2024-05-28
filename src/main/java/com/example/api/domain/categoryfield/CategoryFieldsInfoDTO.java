package com.example.api.domain.categoryfield;

import com.example.api.domain.fields.DataType;
import com.example.api.domain.fields.FieldType;

public record CategoryFieldsInfoDTO(
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
}
