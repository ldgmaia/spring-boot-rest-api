package com.example.api.domain.mpnfieldsvalues;

import com.example.api.domain.fieldsvalues.FieldValueInfoDTO;

public record MPNFieldValueInfoDTOOLD(
        Long id,
        FieldValueInfoDTO fieldValue
//        MPNInfoDTO mpnInfoDTO
) {
    public MPNFieldValueInfoDTOOLD(MPNFieldsValues mpnFieldsValues) {
        this(
                mpnFieldsValues.getId(),
                new FieldValueInfoDTO(mpnFieldsValues.getFieldValue())
//                new MPNInfoDTO(mpnFieldsValues.getMpn())
        );
    }
}


