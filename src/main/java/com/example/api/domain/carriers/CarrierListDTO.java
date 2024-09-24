package com.example.api.domain.carriers;

public record CarrierListDTO(Long id, String name, Boolean enabled) {

    public CarrierListDTO(Carrier carrier) {
        this(carrier.getId(), carrier.getName(), carrier.getEnabled());
    }
}
