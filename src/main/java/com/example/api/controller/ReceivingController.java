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
//    public ResponseEntity register(@RequestBody @Valid ReceivingRequestDTO data, UriComponentsBuilder uriBuilder) {

//    public ResponseEntity register(@ModelAttribute @Valid ReceivingRequestDTO data, UriComponentsBuilder uriBuilder) throws IOException {
    public ResponseEntity register(
            @RequestPart("data") @Valid ReceivingRequestDTO data,  // For complex form data
            @RequestPart("pictures") MultipartFile[] pictures,
            UriComponentsBuilder uriBuilder) throws IOException {

        System.out.println("data " + data);
//        System.out.println("pictures " + pictures);


        var receiving = receivingService.register(data, pictures);
//        var uri = uriBuilder.path("/receiving/{id}").buildAndExpand(receiving.identifier()).toUri();
//        return ResponseEntity.created(uri).body(receiving);
        return ResponseEntity.ok("ok");

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
