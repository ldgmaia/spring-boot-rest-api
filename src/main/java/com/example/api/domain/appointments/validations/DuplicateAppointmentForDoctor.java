package com.example.api.domain.appointments.validations;

import com.example.api.domain.ValidationException;
import com.example.api.domain.appointments.AppointmentCreateDTO;
import com.example.api.domain.appointments.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DuplicateAppointmentForDoctor implements AppointmentBookingValidator {

    @Autowired
    private AppointmentRepository appointmentRepository;

    public void validate(AppointmentCreateDTO data) {

        var doctorAvailableDateTime = appointmentRepository.existsByDoctorIdAndDate(data.doctorId(), data.date());

        if (doctorAvailableDateTime) {
            throw new ValidationException("Doctor already booked for this date and time");
        }

    }
}
