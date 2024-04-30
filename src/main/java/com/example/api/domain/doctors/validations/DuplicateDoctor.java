package com.example.api.domain.doctors.validations;

import com.example.api.domain.ValidationException;
import com.example.api.domain.doctors.DoctorRegisterDTO;
import com.example.api.repositories.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DuplicateDoctor implements DoctorValidator {

    @Autowired
    private DoctorRepository doctorRepository;

    public void validate(DoctorRegisterDTO data) {

        var doctorNameFound = doctorRepository.existsByName(data.name());

        if (doctorNameFound) {
            throw new ValidationException("Doctor name already in record");
        }

    }
}
