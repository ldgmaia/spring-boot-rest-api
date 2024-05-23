package com.example.api.domain.categoryfield;

import com.example.api.domain.categories.DataLevel;
import jakarta.validation.constraints.NotNull;

public record CategoryFieldRequestDTO(
        @NotNull
        DataLevel dataLevel,

//        @NotNull
//        Category category,

        @NotNull
        Long fieldId,

        Boolean printOnLabel,
        Boolean isMandatory

) {
}
