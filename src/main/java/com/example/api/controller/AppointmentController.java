package com.example.api.controller;

import com.example.api.domain.appointments.AppointmentBooking;
import com.example.api.domain.appointments.AppointmentCreateDTO;
import com.example.api.domain.appointments.AppointmentDetailsDTO;
import com.example.api.domain.doctors.DoctorRepository;
import com.example.api.domain.patients.PatientRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("appointments")
@SecurityRequirement(name = "bearer-key")
public class AppointmentController {

    @Autowired
    private AppointmentBooking appointmentBooking;

    @PostMapping
    @Transactional
    public ResponseEntity bookAppointment(@RequestBody @Valid AppointmentCreateDTO data) {

        var appointment = appointmentBooking.booking(data);

        return ResponseEntity.ok(appointment);
    }
}
