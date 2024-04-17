package com.example.api.domain.users;

import java.time.LocalDateTime;

public record AuthenticationDTO(
        String username,
        String password,
        String first_name,
        String last_name,
        Boolean enabled,
        LocalDateTime created_at,
        LocalDateTime updated_at
) {
}
