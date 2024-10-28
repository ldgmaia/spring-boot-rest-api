package com.example.api.controller;

import com.example.api.domain.receivings.Receiving;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

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
    public ResponseEntity register(
            @RequestPart("data") @Valid ReceivingRequestDTO data,  // For complex form data
            @RequestPart("pictures") MultipartFile[] pictures,
            UriComponentsBuilder uriBuilder) throws IOException {

        var receiving = receivingService.register(data, pictures);
        var uri = uriBuilder.path("/receiving/{id}").buildAndExpand(receiving.id()).toUri();
        return ResponseEntity.created(uri).body(receiving);
    }

    @GetMapping
    public ResponseEntity<Page<ReceivingListDTO>> list(@PageableDefault(size = 100, page = 0, sort = {"id"}) Pageable pagination) {
//        Page<Receiving> page = receivingRepository.findAll(pagination);
        Page<Receiving> page = receivingRepository.findAllWithPictures(pagination);

        Page<ReceivingListDTO> dtoPage = page.map(ReceivingListDTO::new);

        return ResponseEntity.ok(dtoPage);
//        var page = receivingRepository.findAll(pagination).map(ReceivingListDTO::new);
////        page.getContent().forEach(System.out::println); // Inspect the data here
//        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity detail(@PathVariable Long id) {
        var receiving = receivingService.show(id);

        return ResponseEntity.ok(receiving);
    }
}
