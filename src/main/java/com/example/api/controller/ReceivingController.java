package com.example.api.controller;

import com.example.api.domain.receivings.ReceivingRequestDTO;
import com.example.api.domain.receivings.ReceivingService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("receivings")
@SecurityRequirement(name = "bearer-key")
public class ReceivingController {

    @Autowired
    private ReceivingService receivingService;

    @PostMapping
    @Transactional
    public ResponseEntity register(@RequestBody @Valid ReceivingRequestDTO data, UriComponentsBuilder uriBuilder) {

        var receiving = receivingService.register(data);
        var uri = uriBuilder.path("/receiving/{id}").buildAndExpand(receiving.identifier()).toUri();
        return ResponseEntity.created(uri).body(receiving);

    }

}
