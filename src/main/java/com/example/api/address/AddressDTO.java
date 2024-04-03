package com.example.api.address;

import jakarta.validation.constraints.NotBlank;

public record AddressDTO(
        @NotBlank
        String street,
        @NotBlank
        String city,
        @NotBlank
        String postalCode,
        @NotBlank
        String province) {
}
