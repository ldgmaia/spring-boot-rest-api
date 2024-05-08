package com.example.api.domain.fieldsvalues;

import com.example.api.domain.fields.DataType;
import com.example.api.domain.fields.Field;
import com.example.api.domain.fields.FieldType;
import com.example.api.domain.values.ValueInfoDTO;

import java.util.List;

public record FieldValueListDTO(Long id, String name, Boolean enabled, Boolean isMultiple, DataType dataType,
                                FieldType fieldType, List<ValueInfoDTO> values) {

    public FieldValueListDTO(Field field, List<ValueInfoDTO> values) {
        this(field.getId(), field.getName(), field.getEnabled(), field.getIsMultiple(), field.getDataType(), field.getFieldType(), values);
    }
}
