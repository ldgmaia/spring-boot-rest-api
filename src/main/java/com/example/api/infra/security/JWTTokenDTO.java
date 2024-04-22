package com.example.api.infra.security;

public record JWTTokenDTO(
        Long id,
        String username,
        String firstName,
        String lastName,
        String token
) {

}
