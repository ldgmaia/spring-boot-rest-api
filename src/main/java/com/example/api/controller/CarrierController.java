package com.example.api.controller;

import com.example.api.domain.ValidationException;
import com.example.api.domain.carriers.Carrier;
import com.example.api.domain.carriers.CarrierInfoDTO;
import com.example.api.repositories.CarrierRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("carriers")
@SecurityRequirement(name = "bearer-key")
public class CarrierController {

    @Autowired
    private CarrierRepository carrierRepository;

    @GetMapping
    public ResponseEntity list(@RequestParam(required = false) Boolean enabled) {

        List<Carrier> carriers;

        if (enabled != null) {
            carriers = carrierRepository.findByEnabled(enabled);
        } else {
            carriers = carrierRepository.findAll();
        }

        return ResponseEntity.ok(carriers);

//        var carriers = carrierRepository.findAll();
//        return ResponseEntity.ok(carriers);
    }

    @GetMapping("/{id}")
    public ResponseEntity detail(@PathVariable Long id) {

        var carrier = carrierRepository.findById(id).orElseThrow(() -> new ValidationException("Carrier not found"));
        return ResponseEntity.ok(new CarrierInfoDTO(carrier));
    }
}
