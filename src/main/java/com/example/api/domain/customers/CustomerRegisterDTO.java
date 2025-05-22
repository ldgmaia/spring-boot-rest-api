package com.example.api.domain.customers;

public record CustomerRegisterDTO(
        Long customerId,
        String name,
        String company,
        String email,
        String phone,
        String username,
        Address address
) {
}
