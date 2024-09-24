package com.example.api.domain.carriers;

public record CarrierInfoDTO(
        Long id,
        String name,
        Boolean enabled
) {

    public CarrierInfoDTO(Carrier carrier) {
        this(
                carrier.getId(),
                carrier.getName(),
                carrier.getEnabled());
    }
}
