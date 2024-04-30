package com.example.api.domain.doctors.validations;

import com.example.api.domain.doctors.DoctorRegisterDTO;

public interface DoctorValidator {

    void validate(DoctorRegisterDTO data);
}
