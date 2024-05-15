package com.example.api.domain.values;

public record ValueInfoDTO(Long id, String valueData, Boolean enabled, Double score) {

    public ValueInfoDTO(Value value, Double score) {
        this(value.getId(), value.getValueData(), value.getEnabled(), score);
    }
}
