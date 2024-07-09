package com.example.api.domain.mpnfieldsvalues;

import com.example.api.domain.fieldsvalues.FieldValue;
import com.example.api.domain.mpns.MPN;
import jakarta.validation.constraints.NotNull;

public record MPNFieldValueRegisterDTO(
        @NotNull
        FieldValue fieldValue,

        @NotNull
        MPN mpn
) {
}
