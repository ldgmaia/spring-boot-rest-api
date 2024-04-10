package com.example.api.domain.appointments.validations;

import com.example.api.domain.ValidationException;
import com.example.api.domain.appointments.AppointmentCreateDTO;
import com.example.api.domain.appointments.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PatientMultipleAppointmentSameDay implements AppointmentBookingValidator {

    @Autowired
    private AppointmentRepository appointmentRepository;

    public void validate(AppointmentCreateDTO data) {

        var firstTime = data.date().withHour(7);
        var lastTime = data.date().withHour(18);
        var patientHasAnotherAppointment = appointmentRepository.existsByPatientIdAndDateBetween(data.patientId(), firstTime, lastTime);

        if(patientHasAnotherAppointment) {
            throw new ValidationException("Patient already booked for this day");
        }

//        LocalDate appointmentDate = data.date().toLocalDate();
//
//        var isPatientBookedForTheDay = appointmentRepository.existsByPatientIdAndDate(data.patientId(), appointmentDate);
//
//        if(isPatientBookedForTheDay) {
//            throw new ValidationException("Patient already booked for this day");
//        }
    }
}
