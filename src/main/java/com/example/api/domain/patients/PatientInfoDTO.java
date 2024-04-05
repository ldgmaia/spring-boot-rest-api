package com.example.api.domain.patients;

import com.example.api.domain.address.Address;

public record PatientInfoDTO(Long id, String name, String email, String phone, Address address) {

    public PatientInfoDTO(Patient patient) {
        this(patient.getId(), patient.getName(), patient.getEmail(), patient.getPhone(), patient.getAddress());
    }
}
