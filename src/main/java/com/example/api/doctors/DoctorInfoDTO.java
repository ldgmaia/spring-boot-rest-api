package com.example.api.doctors;

import com.example.api.address.Address;

public record DoctorInfoDTO(Long id, String name, String email, String cpso, String phone, Specialty specialty, Address address) {

    public DoctorInfoDTO(Doctor doctor) {
        this(doctor.getId(), doctor.getName(), doctor.getEmail(), doctor.getCpso(), doctor.getPhone(), doctor.getSpecialty(), doctor.getAddress());
    }
}
