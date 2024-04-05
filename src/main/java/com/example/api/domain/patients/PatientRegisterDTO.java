package com.example.api.domain.patients;

import com.example.api.domain.address.AddressDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PatientRegisterDTO(
        @NotBlank
        String name,
        @Email
        String email,

        @NotBlank
        String phone,

        @NotNull
        @Valid
        AddressDTO address) {
}
