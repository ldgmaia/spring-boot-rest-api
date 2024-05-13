package com.example.api.controller;

import com.example.api.domain.patients.*;
import com.example.api.repositories.PatientRepository;
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
@RequestMapping("patients")
@SecurityRequirement(name = "bearer-key")
public class PatientsController {

    @Autowired
    private PatientRepository repository;

    @PostMapping
    @Transactional
    public ResponseEntity register(@RequestBody @Valid PatientRegisterDTO data, UriComponentsBuilder uriBuilder) {

        var patient = new Patient(data);
        repository.save(patient);

        var uri = uriBuilder.path("/patients/{id}").buildAndExpand(patient.getId()).toUri();

        return ResponseEntity.created(uri).body(new PatientInfoDTO(patient));
    }

    @GetMapping
    public ResponseEntity<Page<PatientListDTO>> list(@PageableDefault(size = 10, page = 0, sort = {"name"}) Pageable pagination) {
//        return repository.findAll(pagination).stream().map(PatientListDTO::new).toList(); // convert from Patient to PatientListDTO and then to List (array). Return of method must be List
//        return repository.findAll(pagination).map(PatientListDTO::new); // add pagination to the query. Return of method must be Page

        var page = repository.findAllByActiveTrue(pagination).map(PatientListDTO::new); // add pagination with only active patients to the query. Return of method must be Page
        return ResponseEntity.ok(page);
    }

    @PutMapping
    @Transactional
    public ResponseEntity update(@RequestBody @Valid PatientUpdateDTO data) {
        var patient = repository.getReferenceById(data.id());
        patient.updateInfo(data);

        return ResponseEntity.ok(new PatientInfoDTO(patient));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity delete(@PathVariable Long id) {
        var patient = repository.getReferenceById(id);
        if (!patient.getActive()) {
            return ResponseEntity.status(409).header("X-Custom-Message", "Patient is already deactivated").build();
        }

        patient.deactivate();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity detail(@PathVariable Long id) {
        var patient = repository.getReferenceById(id);
        return ResponseEntity.ok(new PatientInfoDTO(patient));
    }
}
