package com.example.api.controller;

import com.example.api.domain.inventoryitems.Inventory;
import com.example.api.domain.inventoryitems.InventoryListDTO;
import com.example.api.domain.inventoryitems.InventoryRequestDTO;
import com.example.api.domain.inventoryitems.InventoryService;
import com.example.api.repositories.InventoryRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("inventory-items")
@SecurityRequirement(name = "bearer-key")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private InventoryRepository inventoryRepository;

    @PostMapping
    @Transactional
    public ResponseEntity register(@RequestBody @Valid InventoryRequestDTO data) {

//        var inventory = inventoryService.register(data);
//        var uri = uriBuilder.path("/inventory/{id}")
//                .buildAndExpand(inventory.id())
//                .toUri();
//        return ResponseEntity.created(uri).body(inventory);
        try {
            inventoryService.register(data);
            return ResponseEntity.ok("OK");
        } catch (Exception e) {
            throw new RuntimeException(e); // change this to show a meaningful message
        }
    }

    @GetMapping
    public ResponseEntity<Page<InventoryListDTO>> list(HttpServletRequest request, @PageableDefault(size = 100, page = 0, sort = {"id"}) Pageable pagination, @RequestHeader HttpHeaders headers) {
        var page = inventoryRepository.findAll(pagination)
                .map((Inventory id) -> new InventoryListDTO(id));
        return ResponseEntity.ok(page);
    }

    @DeleteMapping("{id}")
    @Transactional
    public ResponseEntity delete(@PathVariable Long id) {

        var inventoryItemToDelete = inventoryRepository.findById(id).orElse(null);
        if (inventoryItemToDelete == null) {
            Map<String, String> jsonResponse = Map.of("message", "Cannot delete item: It was not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonResponse);
        }

        LocalDateTime itemCreationTime = inventoryItemToDelete.getCreatedAt();

        if (itemCreationTime == null) {
            Map<String, String> jsonResponse = Map.of("message", "Cannot delete item: Creation date is invalid");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonResponse);
        }

        LocalDateTime currentTime = LocalDateTime.now();
        Duration timeDifference = Duration.between(itemCreationTime, currentTime);

        if (timeDifference.toMinutes() > 30) {
            Map<String, String> jsonResponse = Map.of("message", "Cannot delete item: It was created more than 30 minutes ago");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonResponse);
        }

        // This line deletes the item only if the time is under 30 min with the previous validation
        inventoryRepository.deleteById(id);

        return ResponseEntity.ok(id);
    }
}
