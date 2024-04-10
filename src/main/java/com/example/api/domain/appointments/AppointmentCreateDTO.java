package com.example.api.domain.appointments;

import com.example.api.domain.doctors.Specialty;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AppointmentCreateDTO(
        Long doctorId,

        @NotNull
        Long patientId,

        @NotNull
        @Future
        LocalDateTime date,

        Specialty specialty
) {
}
