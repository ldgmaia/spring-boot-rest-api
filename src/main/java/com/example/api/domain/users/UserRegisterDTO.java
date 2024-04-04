package com.example.api.domain.users;

import jakarta.validation.constraints.NotBlank;

public record UserRegisterDTO(
        @NotBlank
        String username,

        @NotBlank
        String password
) {

}
