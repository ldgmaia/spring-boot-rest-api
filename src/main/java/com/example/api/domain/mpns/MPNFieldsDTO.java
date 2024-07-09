package com.example.api.domain.mpns;

import com.example.api.domain.values.ValueInfoDTO;

import java.util.List;

public record MPNFieldsDTO(
        Long id,
        String name,
        List<ValueInfoDTO> values
) {
    public MPNFieldsDTO(Long id, String name) {
        this(id, name, List.of()); // Default to an empty list if values are not provided
    }
}
