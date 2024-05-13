package com.example.api.domain.fields;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record FieldsByGroupDTO(
        @NotBlank
        String groupName,

        List<FieldListDTO> fields
) {
}
