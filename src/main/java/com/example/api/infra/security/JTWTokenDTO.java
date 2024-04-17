package com.example.api.infra.security;

public record JTWTokenDTO(
        Long id,
        String username,
        String firstName,
        String lastName,
        String token
) {

}
