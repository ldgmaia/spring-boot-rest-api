package com.example.api.domain.users;

import jakarta.validation.constraints.NotBlank;

public record AuthenticationDTO(
        @NotBlank
        String username,
        @NotBlank
        String password

//        String first_name,
//        String last_name,
//        Boolean enabled,
//        LocalDateTime created_at,
//        LocalDateTime updated_at
) {
}
