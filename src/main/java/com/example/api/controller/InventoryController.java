package com.example.api.controller;

import com.example.api.domain.categories.CategoryService;
import com.example.api.domain.inventory.Inventory;
import com.example.api.domain.inventory.InventoryListDTO;
import com.example.api.domain.inventory.InventoryRequestDTO;
import com.example.api.domain.inventory.InventoryService;
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
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("inventory")
@SecurityRequirement(name = "bearer-key")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private CategoryService categoryService;


    @PostMapping
    @Transactional
    public ResponseEntity register(@RequestBody @Valid InventoryRequestDTO data, UriComponentsBuilder uriBuilder) {

        System.out.println("data on controller: " + data);
        var inventory = inventoryService.register(data);
        var uri = uriBuilder.path("/inventory/{id}")
                .buildAndExpand(inventory.id())
                .toUri();
        return ResponseEntity.created(uri).body(inventory);

    }


    @GetMapping
    public ResponseEntity<Page<InventoryListDTO>> list(HttpServletRequest request, @PageableDefault(size = 100, page = 0, sort = {"name"}) Pageable pagination, @RequestHeader HttpHeaders headers) {
        var page = inventoryRepository.findAll(pagination)
                .map((Inventory id) -> new InventoryListDTO(id));
        return ResponseEntity.ok((Page<InventoryListDTO>) (Page<InventoryListDTO>) page);
    }


    @DeleteMapping("/delete/{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        var itemToDelete = inventoryRepository.findById(id);

        // Check if the item is present
        if (itemToDelete.isEmpty()) {
            System.err.println("// If not found, return a 404 Not Found response");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } else {
            System.out.println("// If found, delete the item by ID");
            // If found, delete the item by ID
            inventoryRepository.deleteById(id);
            System.out.println("Item " + itemToDelete.toString() + " was deleted from inventory");
        }
        return ResponseEntity.noContent().build();
    }

}
