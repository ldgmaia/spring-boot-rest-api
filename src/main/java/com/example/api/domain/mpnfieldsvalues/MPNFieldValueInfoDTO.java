package com.example.api.domain.mpnfieldsvalues;

import com.example.api.domain.fields.DataType;

public record MPNFieldValueInfoDTO(
        Long id,
        Long fieldId,
        String fieldName,
        Long valueDataId,
        String valueData,
        DataType dataType
) {

}
