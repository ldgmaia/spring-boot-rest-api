package com.example.api.domain.appointments.validations;

import com.example.api.domain.ValidationException;
import com.example.api.domain.appointments.AppointmentCreateDTO;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class AppointmentTimeInAdvance implements AppointmentBookingValidator {

    public void validate(AppointmentCreateDTO data) {
        var appointmentDate = data.date();

        var timeInAdvance = Duration.between(LocalDateTime.now(), appointmentDate).toMinutes();

        if(timeInAdvance < 30) {
            throw new ValidationException("Appointment must be booked at least 30 min in advance");
        }
    }
}
