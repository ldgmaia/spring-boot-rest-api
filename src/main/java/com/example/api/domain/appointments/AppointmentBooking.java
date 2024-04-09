package com.example.api.domain.appointments;

import com.example.api.domain.ValidationException;
import com.example.api.domain.doctors.Doctor;
import com.example.api.domain.doctors.DoctorRepository;
import com.example.api.domain.patients.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppointmentBooking {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    public void booking(AppointmentCreateDTO data) {

        if(!patientRepository.existsById(data.patientId())) {
            throw new ValidationException("Patient ID not valid");
        }

        if(data.doctorId() != null && !doctorRepository.existsById(data.doctorId())) {
            throw new ValidationException("Doctor ID not valid");
        }

        var patient = patientRepository.getReferenceById(data.patientId());
//        var doctor = doctorRepository.findById(data.doctorId()).get();
        var doctor = pickDoctor(data);
        var appointment = new Appointment(null, doctor, patient, data.date());

        appointmentRepository.save(appointment);

    }

    private Doctor pickDoctor(AppointmentCreateDTO data) {
        if(data.doctorId() != null) {
            return doctorRepository.getReferenceById(data.doctorId());
        }

        if (data.specialty() == null) {
            throw new ValidationException("Specialty is mandatory when no doctor is selected");
        }

        return doctorRepository.pickAvailableDoctor(data.specialty(), data.date());
    }

}
