package com.example.api.domain.orders;

public record CustomerAddressDTO(
        String name,
        String company,
        String street1,
        String street2,
        String street3,
        String city,
        String state,
        String postalCode,
        String country,
        String phone,
        Boolean residential,
        String addressVerified
) {
}
