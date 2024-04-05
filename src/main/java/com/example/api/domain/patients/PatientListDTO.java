package com.example.api.domain.patients;

public record PatientListDTO(Long id, String name, String email) {

    public PatientListDTO(Patient patient) {
        this(patient.getId(), patient.getName(), patient.getEmail());
    }


}
