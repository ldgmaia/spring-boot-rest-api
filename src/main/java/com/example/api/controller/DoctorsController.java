package com.example.api.controller;


import com.example.api.doctors.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("doctors")
public class DoctorsController {

    @Autowired
    private DoctorRepository repository;

    @PostMapping
    @Transactional
    public void register(@RequestBody @Valid DoctorRegisterDTO data) {
        repository.save(new Doctor(data));
    }

    @GetMapping
    public Page<DoctorListDTO> list(@PageableDefault(size = 10, page = 0, sort = {"name"}) Pageable pagination) {
//        return repository.findAll(pagination).stream().map(DoctorListDTO::new).toList(); // convert from Doctor to DoctorListDTO and then to List (array). Return of method must be List
//        return repository.findAll(pagination).map(DoctorListDTO::new); // add pagination to the query. Return of method must be Page
        return repository.findAllByActiveTrue(pagination).map(DoctorListDTO::new); // add pagination to the query. Return of method must be Page
    }

    @PutMapping
    @Transactional
    public void update(@RequestBody @Valid DoctorUpdateDTO data) {
        var doctor = repository.getReferenceById(data.id());
        doctor.updateInfo(data);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public void delete(@PathVariable Long id) {
//        repository.deleteById(id); // hard delete from database
        var doctor = repository.getReferenceById(id);
        doctor.deactivate();
    }
}
