package com.example.api.domain.fields;

public record FieldListDTO(Long id, String name, Boolean enabled, Boolean isMultiple, DataType dataType,
                           FieldType fieldType) {

    public FieldListDTO(Field field) {
        this(field.getId(), field.getName(), field.getEnabled(), field.getIsMultiple(), field.getDataType(), field.getFieldType());
    }
}
