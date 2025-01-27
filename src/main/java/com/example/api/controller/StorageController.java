package com.example.api.controller;

import com.example.api.domain.storage.StorageRequestDTO;
import com.example.api.domain.storage.StorageService;
import com.example.api.domain.storage.StorageUpdateDTO;
import com.example.api.domain.storage.storagezone.StorageZoneInfoDTO;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/storage")
@SecurityRequirement(name = "bearer-key")
public class StorageController {

    @Autowired
    private StorageService storageService;

    @Transactional
    @Modifying
    @PostMapping
    public ResponseEntity<StorageZoneInfoDTO> createStorage(@RequestBody StorageRequestDTO request) {
        var savedZone = storageService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedZone);
    }

    @GetMapping
    public ResponseEntity<List<StorageZoneInfoDTO>> getAllStorage() {
        List<StorageZoneInfoDTO> zones = storageService.getAllZonesInfo();
        return ResponseEntity.ok(zones);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity updateStorage(@PathVariable Long id, @RequestBody StorageUpdateDTO request) {
        storageService.update(id, request);

        return ResponseEntity.ok("OK");
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<StorageLevelLocationDisplayDTO> getLocationById(@PathVariable Long id) {
//        return ResponseEntity.ok(storageService.getLocationDisplayById(id));
//    }
}
