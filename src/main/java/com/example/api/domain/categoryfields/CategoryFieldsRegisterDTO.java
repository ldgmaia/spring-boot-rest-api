package com.example.api.domain.categoryfields;

import com.example.api.domain.categories.Category;
import com.example.api.domain.fields.Field;
import jakarta.validation.constraints.NotNull;

public record CategoryFieldsRegisterDTO(
        @NotNull
        DataLevel dataLevel,

        @NotNull
        Category category,

        @NotNull
        Field field,

        Boolean printOnLabel,
        Boolean isMandatory
) {
}
