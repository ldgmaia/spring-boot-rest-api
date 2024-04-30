package com.example.api.domain.fields;

import com.example.api.domain.fieldgroups.FieldGroupInfoDTO;

public record FieldInfoDTO(Long id, String name, Boolean enabled, Boolean isMultiple, DataType dataType,
                           FieldType fieldType, FieldGroupInfoDTO fieldGroupId) {

    public FieldInfoDTO(Field field) {
        this(field.getId(), field.getName(), field.getEnabled(), field.getIsMultiple(), field.getDataType(), field.getFieldType(), new FieldGroupInfoDTO(field.getFieldGroup()));
    }
}
