package com.example.api.controller;

import com.example.api.domain.receivings.*;
import com.example.api.repositories.InventoryItemRepository;
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

    @Autowired
    private InventoryItemRepository inventoryItemRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<ReceivingInfoDTO> register(
            @RequestPart("data") @Valid ReceivingRequestDTO data,  // For complex form data
            @RequestPart("pictures") MultipartFile[] pictures,
            UriComponentsBuilder uriBuilder) throws IOException {

        var receiving = receivingService.register(data, pictures);
        var uri = uriBuilder.path("/receiving/{id}").buildAndExpand(receiving.id()).toUri();
        return ResponseEntity.created(uri).body(receiving);
    }

    @GetMapping
    public ResponseEntity<Page<ReceivingListDTO>> list(@PageableDefault(size = 100, page = 0, sort = {"id"}) Pageable pagination) {

        // Fetch paginated data from repository
        Page<Receiving> receivingsPage = receivingRepository.findAllWithPictures(pagination);

        // Map each Receiving to ReceivingListDTO
        Page<ReceivingListDTO> receivings = receivingsPage.map(receiving ->
                new ReceivingListDTO(receiving, inventoryItemRepository)
        );

        // Return the paginated DTOs
        return ResponseEntity.ok(receivings);

//        Page<Receiving> page = receivingRepository.findAllWithPictures(pagination);
//        Page<ReceivingListDTO> dtoPage = page.map(ReceivingListDTO::new);
//        return ResponseEntity.ok(dtoPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReceivingInfoDTO> detail(@PathVariable Long id) {
        var receiving = receivingService.show(id);

        return ResponseEntity.ok(receiving);
    }
}
