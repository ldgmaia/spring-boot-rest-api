package com.example.api.domain.appointments.validations;

import com.example.api.domain.ValidationException;
import com.example.api.domain.appointments.AppointmentCreateDTO;
import com.example.api.repositories.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InactivePatient implements AppointmentBookingValidator {

    @Autowired
    private PatientRepository patientRepository;

    public void validate(AppointmentCreateDTO data) {

        System.out.println("I am here");
        var patient = patientRepository.findActiveById(data.patientId());

        if (!patient) {
            throw new ValidationException("Appointment cannot be booked for inactive patient");
        }
    }
}
