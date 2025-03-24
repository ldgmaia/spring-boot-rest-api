package com.example.api.infra.security;

import java.util.List;

public record JWTTokenDTO(
        Long userId,
        String username,
        String firstName,
        String lastName,
        Long storageLevelId,
        List<String> roles,
        String token
) {

}
