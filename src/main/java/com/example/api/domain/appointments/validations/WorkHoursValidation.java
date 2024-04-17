package com.example.api.domain.appointments.validations;

import com.example.api.domain.ValidationException;
import com.example.api.domain.appointments.AppointmentCreateDTO;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;

@Component
public class WorkHoursValidation implements AppointmentBookingValidator {

    public void validate(AppointmentCreateDTO data) {
        var appointmentDate = data.date();

        var sunday = appointmentDate.getDayOfWeek().equals(DayOfWeek.SUNDAY);
        var notWorkHours = appointmentDate.getHour() < 7 || appointmentDate.getHour() > 18;

        if (sunday || notWorkHours) {
            throw new ValidationException("Appointment out of hours");
        }
    }
}
