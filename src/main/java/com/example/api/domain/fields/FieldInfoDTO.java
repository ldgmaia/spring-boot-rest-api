package com.example.api.domain.fields;

import com.example.api.domain.fieldgroups.FieldGroupInfoDTO;

import java.util.Optional;

public record FieldInfoDTO(Long id, String name, Boolean enabled, Boolean isMultiple, DataType dataType,
                           FieldType fieldType, FieldGroupInfoDTO fieldGroup) {

    public FieldInfoDTO(Field field) {
        this(
                field.getId(),
                field.getName(),
                field.getEnabled(),
                field.getIsMultiple(),
                field.getDataType(),
                field.getFieldType(),
//                field.getFieldGroup() != null ? new FieldGroupInfoDTO(field.getFieldGroup()) : null); // another way of doing the same check
                Optional.ofNullable(field.getFieldGroup()).map(FieldGroupInfoDTO::new).orElse(null)
        );
    }
}
