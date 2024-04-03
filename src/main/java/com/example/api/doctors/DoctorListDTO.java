package com.example.api.doctors;

public record DoctorListDTO(Long id, String name, String email, String cpso, Specialty specialty) {

    public DoctorListDTO(Doctor doctor) {
        this(doctor.getId(), doctor.getName(), doctor.getEmail(), doctor.getCpso(), doctor.getSpecialty() );
    }


}
