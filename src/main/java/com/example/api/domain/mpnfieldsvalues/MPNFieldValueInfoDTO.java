package com.example.api.domain.mpnfieldsvalues;

public record MPNFieldValueInfoDTO(
        Long id,
        Long fieldId,
        String fieldName,
        Long valueDataId,
        String valueData
) {
//    public MPNFieldValueInfoDTO(MPNFieldsValues mpnFieldsValues) {
//        this(
//                mpnFieldsValues.getId(),
//                new FieldValueInfoDTO(mpnFieldsValues.getFieldValue())
////                new MPNInfoDTO(mpnFieldsValues.getMpn())
//        );
//    }
}


