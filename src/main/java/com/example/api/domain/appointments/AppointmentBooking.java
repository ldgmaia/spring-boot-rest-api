package com.example.api.domain.appointments;

import com.example.api.domain.ValidationException;
import com.example.api.domain.appointments.validations.AppointmentBookingValidator;
import com.example.api.domain.doctors.Doctor;
import com.example.api.repositories.AppointmentRepository;
import com.example.api.repositories.DoctorRepository;
import com.example.api.repositories.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentBooking {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private List<AppointmentBookingValidator> validators; // Spring boot will automatically detect that a List is being ejected and will get all classes that implements this interface and will inject the validators automatically

    public AppointmentDetailsDTO booking(AppointmentCreateDTO data) {

        if (!patientRepository.existsById(data.patientId())) {
            throw new ValidationException("Patient ID not valid");
        }

        if (data.doctorId() != null && !doctorRepository.existsById(data.doctorId())) {
            throw new ValidationException("Doctor ID not valid");
        }

        validators.forEach(v -> v.validate(data));

        var patient = patientRepository.getReferenceById(data.patientId());

        //        var doctor = doctorRepository.findById(data.doctorId()).get();
        var doctor = pickDoctor(data);

        if (doctor == null) {
            throw new ValidationException("No Doctor available on this date for this specialty");
        }

        var appointment = new Appointment(null, doctor, patient, data.date());

        appointmentRepository.save(appointment);

        return new AppointmentDetailsDTO(appointment);

    }

    private Doctor pickDoctor(AppointmentCreateDTO data) {
        if (data.doctorId() != null) {
            return doctorRepository.getReferenceById(data.doctorId());
        }

        if (data.specialty() == null) {
            throw new ValidationException("Specialty is mandatory when no doctor is selected");
        }

        return doctorRepository.pickAvailableDoctor(data.specialty(), data.date());
    }

}
