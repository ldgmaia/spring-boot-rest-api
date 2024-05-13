package com.example.api.infra.security;

import java.util.List;

public record JWTTokenDTO(
        Long userId,
        String username,
        String firstName,
        String lastName,
        List<String> roles,
        String token
) {

}
