package com.example.api.domain.appointments;

import java.time.LocalDateTime;

public record AppointmentDetailsDTO(Long id, Long doctorId, Long patientId, LocalDateTime date) {
}
