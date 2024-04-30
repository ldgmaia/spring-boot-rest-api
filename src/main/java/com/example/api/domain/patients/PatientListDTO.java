package com.example.api.domain.patients;

import com.example.api.domain.address.Address;

public record PatientListDTO(Long id, String name, String email, Address address) {

    public PatientListDTO(Patient patient) {
        this(patient.getId(), patient.getName(), patient.getEmail(), patient.getAddress());
    }


}
