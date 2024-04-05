package com.example.api.domain.patients;

import com.example.api.domain.address.AddressDTO;
import jakarta.validation.constraints.NotNull;

public record PatientUpdateDTO(
        @NotNull
        Long id,
        String name,
        String phone,
        AddressDTO address) {
}
