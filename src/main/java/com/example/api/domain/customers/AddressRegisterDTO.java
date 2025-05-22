package com.example.api.domain.customers;

public record AddressRegisterDTO(
        String street1,
        String street2,
        String street3,
        String city,
        String state,
        String postalCode,
        String country
) {
}
