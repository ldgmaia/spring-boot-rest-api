package com.example.api.controller;

import com.example.api.domain.inventoryitems.*;
import com.example.api.repositories.InventoryItemRepository;
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
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("inventory-items")
@SecurityRequirement(name = "bearer-key")
public class InventoryItemController {

    @Autowired
    private InventoryItemService inventoryItemService;

    @Autowired
    private InventoryItemRepository inventoryRepository;


    @PostMapping
    @Transactional
    public ResponseEntity register(@RequestBody @Valid InventoryItemRequestDTO data) {

//        var inventory = inventoryService.register(data);
//        var uri = uriBuilder.path("/inventory/{id}")
//                .buildAndExpand(inventory.id())
//                .toUri();
//        return ResponseEntity.created(uri).body(inventory);
//        System.out.println("data " + data);
//        return ResponseEntity.ok("OK");

        try {
            var newInventoryItems = inventoryItemService.register(data);
            return ResponseEntity.ok(newInventoryItems);
        } catch (Exception e) {
            throw new RuntimeException(e); // change this to show a meaningful message
        }
    }

    @GetMapping
    public ResponseEntity<Page<InventoryInfoDTO>> list(HttpServletRequest request, @PageableDefault(size = 100, page = 0, sort = {"id"}) Pageable pagination, @RequestHeader HttpHeaders headers) {
        var page = inventoryRepository.findAll(pagination)
                .map((InventoryItem inventoryItem) -> new InventoryInfoDTO(inventoryItem));
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

    @GetMapping("/by-receiving-item/{receivingItemId}")
    public ResponseEntity getInventoryItemsByReceivingItemId(@PathVariable Long receivingItemId) {
        List<InventoryItemsByReceivingItemDTO> items = inventoryItemService.getInventoryItemsByReceivingItemId(receivingItemId);

        return ResponseEntity.ok(items);
    }

}
