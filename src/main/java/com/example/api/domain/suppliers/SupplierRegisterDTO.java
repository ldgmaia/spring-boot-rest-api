package com.example.api.domain.suppliers;

import jakarta.validation.constraints.NotBlank;

public record SupplierRegisterDTO(
        @NotBlank
        String name,

        String phone,
        String email,
        String company,
        String street1,
        String street2,
        String street3,
        String city,
        String state,
        String postalCode,
        String country
) {
}
