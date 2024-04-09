package com.example.api.domain.appointments;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    boolean existsByDoctorIdAndDate(Long doctorId, LocalDateTime date);

//    Boolean existsByPatientIdAndDate(Long patientId, LocalDate date);
    boolean existsByPatientIdAndDateBetween(Long patientId, LocalDateTime firstTime, LocalDateTime lastTime);
}
