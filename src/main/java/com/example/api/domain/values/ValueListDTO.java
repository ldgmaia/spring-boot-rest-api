package com.example.api.domain.values;

public record ValueListDTO(Long id, String valueData, Boolean enabled) {

    public ValueListDTO(Value value) {
        this(value.getId(), value.getValueData(), value.getEnabled());
    }


}
