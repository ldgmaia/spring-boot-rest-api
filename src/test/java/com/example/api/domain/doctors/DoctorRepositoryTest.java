package com.example.api.domain.doctors;

import com.example.api.domain.address.AddressDTO;
import com.example.api.domain.appointments.Appointment;
import com.example.api.domain.patients.Patient;
import com.example.api.domain.patients.PatientRegisterDTO;
import com.example.api.repositories.DoctorRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
// the line above is to set the test database to use the same connection as production
@ActiveProfiles("test")
class DoctorRepositoryTest {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private TestEntityManager em;

    @Test
    @DisplayName("It should return null when the one doctor registered is not available on the date")
    void pickAvailableDoctorScenario1() {

        //Given
        var nextMonday10am = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)).atTime(10, 0);
        var doctor = registerDoctor("Doctor", "doctor@email.com", "123456", Specialty.CARDIOLOGY);
        var patient = registerPatient("English Patient", "patient@email.com");
        appointmentRegistration(doctor, patient, nextMonday10am);

        // When
        var doctorAvailable = doctorRepository.pickAvailableDoctor(Specialty.CARDIOLOGY, nextMonday10am);

        // Then
        assertThat(doctorAvailable).isNull();
    }

    @Test
    @DisplayName("It should return doctor when the doctor is available")
    void pickAvailableDoctorScenario2() {
        var nextMonday10am = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)).atTime(10, 0);

        var doctor = registerDoctor("Doctor", "doctor@email.com", "123456", Specialty.CARDIOLOGY);

        var doctorAvailable = doctorRepository.pickAvailableDoctor(Specialty.CARDIOLOGY, nextMonday10am);

        assertThat(doctorAvailable).isEqualTo(doctor);
    }

    private void appointmentRegistration(Doctor doctor, Patient patient, LocalDateTime date) {
        em.persist(new Appointment(null, doctor, patient, date));
    }

    private Doctor registerDoctor(String name, String email, String cpso, Specialty specialty) {
        var doctor = new Doctor(doctor(name, email, cpso, specialty));
        em.persist(doctor);
        return doctor;
    }

    private Patient registerPatient(String name, String email) {
        var patient = new Patient(patient(name, email));
        em.persist(patient);
        return patient;
    }

    private DoctorRegisterDTO doctor(String name, String email, String cpso, Specialty especialty) {
        return new DoctorRegisterDTO(name, email, "555123456", cpso, especialty, address());
    }

    private PatientRegisterDTO patient(String name, String email) {
        return new PatientRegisterDTO(name, email, "555123456", address());
    }

    private AddressDTO address() {
        return new AddressDTO("123 That Street", "Neverland", "1A2B3C", "ON");
    }

}
