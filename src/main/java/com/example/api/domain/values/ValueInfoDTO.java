package com.example.api.domain.values;

import java.util.Objects;

public record ValueInfoDTO(
        Long id,
        String valueData,
        Boolean enabled,
        Double score
) {
    public ValueInfoDTO(Value value) {
        this(
                value.getId(),
                value.getValueData(),
                value.getEnabled(),
                value.getFieldValues() != null ?
                        value.getFieldValues().stream()
                                .map(fieldValue -> fieldValue.getScore()) // Map to score
                                .filter(Objects::nonNull) // Filter out null scores
                                .findFirst() // Find the first non-null score
                                .orElse(null) : // Return null if no score is found
                        null // Return null if fieldValues is null
        );
    }
}
