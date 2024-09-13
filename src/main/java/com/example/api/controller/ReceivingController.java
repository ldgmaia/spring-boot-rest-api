package com.example.api.controller;

import com.example.api.domain.receivings.ReceivingListDTO;
import com.example.api.domain.receivings.ReceivingRequestDTO;
import com.example.api.domain.receivings.ReceivingService;
import com.example.api.repositories.ReceivingRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("receivings")
@SecurityRequirement(name = "bearer-key")
public class ReceivingController {

    @Autowired
    private ReceivingService receivingService;

    @Autowired
    private ReceivingRepository receivingRepository;

    @PostMapping
    @Transactional
    public ResponseEntity register(@RequestBody @Valid ReceivingRequestDTO data, UriComponentsBuilder uriBuilder) {

        var receiving = receivingService.register(data);
        var uri = uriBuilder.path("/receiving/{id}").buildAndExpand(receiving.identifier()).toUri();
        return ResponseEntity.created(uri).body(receiving);

    }

    @GetMapping
    public ResponseEntity<Page<ReceivingListDTO>> list(@PageableDefault(size = 100, page = 0, sort = {"id"}) Pageable pagination, @RequestHeader HttpHeaders headers) {
        var page = receivingRepository.findAll(pagination).map(ReceivingListDTO::new);

//        page.getContent().forEach(System.out::println); // Inspect the data here
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity detail(@PathVariable Long id) {
        var receivingById = receivingService.show(id);

        return ResponseEntity.ok(receivingById);
    }


}
