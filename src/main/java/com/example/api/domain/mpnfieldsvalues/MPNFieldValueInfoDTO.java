package com.example.api.domain.mpnfieldsvalues;

import com.example.api.domain.fieldsvalues.FieldValueInfoDTO;
import com.example.api.domain.mpns.MPNInfoDTO;

public record MPNFieldValueInfoDTO(
        Long id,
        FieldValueInfoDTO fieldValue,
        MPNInfoDTO mpnInfoDTO
) {
    public MPNFieldValueInfoDTO(MPNFieldsValues mpnFieldsValues) {
        this(
                mpnFieldsValues.getId(),
                new FieldValueInfoDTO(mpnFieldsValues.getFieldValue()),
                new MPNInfoDTO(mpnFieldsValues.getMpn())
        );
    }
}


