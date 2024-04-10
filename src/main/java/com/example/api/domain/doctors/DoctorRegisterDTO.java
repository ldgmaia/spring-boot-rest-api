package com.example.api.domain.doctors;

import com.example.api.domain.address.AddressDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record DoctorRegisterDTO(
        @NotBlank
        String name,

        @Email
        String email,

        @NotBlank
        String phone,

        @NotBlank
        @Pattern(regexp = "\\d{4,6}")
        String cpso,

        @NotNull
        Specialty specialty,

        @NotNull
        @Valid
        AddressDTO address

//        @Future
//        LocalDateTime createdAt,
//
//        @Future
//        LocalDateTime updatedAt
) {
}
