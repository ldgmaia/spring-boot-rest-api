package com.example.api.domain.doctors;

import com.example.api.domain.doctors.validations.DoctorValidator;
import com.example.api.repositories.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private List<DoctorValidator> validators; // Spring boot will automatically detect that a List is being ejected and will get all classes that implements this interface and will inject the validators automatically

    public DoctorInfoDTO register(DoctorRegisterDTO data) {

        validators.forEach(v -> v.validate(data));
        var doctor = new Doctor(data);
        doctorRepository.save(doctor);

        return new DoctorInfoDTO(doctor);
    }
}
