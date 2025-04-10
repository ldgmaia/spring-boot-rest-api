package com.example.api.domain.fieldsvalues;

import com.example.api.domain.fields.FieldInfoDTO;
import com.example.api.domain.values.ValueInfoDTO;

public record FieldValueInfoDTO(
        Long id,
        FieldInfoDTO field,
        ValueInfoDTO valueData,
        Double score
) {
    public FieldValueInfoDTO(FieldValue fieldValue) {
        this(
                fieldValue.getId(),
                new FieldInfoDTO(fieldValue.getField()),
                new ValueInfoDTO(fieldValue.getValueData(), fieldValue.getScore()),
                fieldValue.getScore()
        );
    }
}
