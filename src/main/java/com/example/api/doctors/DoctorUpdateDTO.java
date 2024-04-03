package com.example.api.doctors;

import com.example.api.address.AddressDTO;
import jakarta.validation.constraints.NotNull;

public record DoctorUpdateDTO(
        @NotNull
        Long id,
        String name,
        String phone,
        AddressDTO address) {
}
