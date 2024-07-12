package com.example.api.domain.values;

public record ValueInfoDTO(Long id, String valueData, Boolean enabled) {

    public ValueInfoDTO(Value value) {
        this(value.getId(), value.getValueData(), value.getEnabled());
    }
}
