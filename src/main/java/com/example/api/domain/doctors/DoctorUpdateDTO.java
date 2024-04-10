package com.example.api.domain.doctors;

import com.example.api.domain.address.AddressDTO;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record DoctorUpdateDTO(
        @NotNull
        Long id,
        String name,
        String phone,
        AddressDTO address,

        @Future
        LocalDateTime updatedAt
) {
}
