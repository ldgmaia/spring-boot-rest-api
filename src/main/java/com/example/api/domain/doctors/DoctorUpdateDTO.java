package com.example.api.domain.doctors;

import com.example.api.domain.address.AddressDTO;
import jakarta.validation.constraints.NotNull;

public record DoctorUpdateDTO(
        @NotNull
        Long id,
        String name,
        String phone,
        AddressDTO address) {
}
