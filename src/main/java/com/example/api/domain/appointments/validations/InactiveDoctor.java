package com.example.api.domain.appointments.validations;

import com.example.api.domain.ValidationException;
import com.example.api.domain.appointments.AppointmentCreateDTO;
import com.example.api.repositories.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InactiveDoctor implements AppointmentBookingValidator {

    @Autowired
    private DoctorRepository doctorRepository;

    public void validate(AppointmentCreateDTO data) {

        if (data.doctorId() == null) {
            return;
        }

        var doctorIsActive = doctorRepository.findActiveById(data.doctorId());

        if (!doctorIsActive) {
            throw new ValidationException("Appointment cannot be booked for inactive doctor");
        }
    }
}
