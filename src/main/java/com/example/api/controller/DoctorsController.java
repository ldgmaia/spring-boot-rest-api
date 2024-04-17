package com.example.api.controller;

import com.example.api.domain.doctors.*;
import com.example.api.repositories.DoctorRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("doctors")
@SecurityRequirement(name = "bearer-key")
public class DoctorsController {

    @Autowired
    private DoctorRepository doctorRepository;

    @PostMapping
    @Transactional
    public ResponseEntity register(@RequestBody @Valid DoctorRegisterDTO data, UriComponentsBuilder uriBuilder) {

        var doctor = new Doctor(data);
        doctorRepository.save(doctor);

        var uri = uriBuilder.path("/doctors/{id}").buildAndExpand(doctor.getId()).toUri();

        return ResponseEntity.created(uri).body(new DoctorInfoDTO(doctor));
    }

    @GetMapping
    public ResponseEntity<Page<DoctorListDTO>> list(@PageableDefault(size = 10, page = 0, sort = {"name"}) Pageable pagination) {
//        return repository.findAll(pagination).stream().map(DoctorListDTO::new).toList(); // convert from Doctor to DoctorListDTO and then to List (array). Return of method must be List
//        return repository.findAll(pagination).map(DoctorListDTO::new); // add pagination to the query. Return of method must be Page

        var page = doctorRepository.findAllByActiveTrue(pagination).map(DoctorListDTO::new); // add pagination with only active doctors to the query. Return of method must be Page
        return ResponseEntity.ok(page);
    }

    @PutMapping
    @Transactional
    public ResponseEntity update(@RequestBody @Valid DoctorUpdateDTO data) {
        var doctor = doctorRepository.getReferenceById(data.id());
        doctor.updateInfo(data);

        return ResponseEntity.ok(new DoctorInfoDTO(doctor));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity delete(@PathVariable Long id) {
//        repository.deleteById(id); // hard delete from database
        var doctor = doctorRepository.getReferenceById(id);
        doctor.deactivate();

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity detail(@PathVariable Long id) {
        var doctor = doctorRepository.getReferenceById(id);
        return ResponseEntity.ok(new DoctorInfoDTO(doctor));
    }
}
