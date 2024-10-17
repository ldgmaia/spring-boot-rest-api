package com.example.api.controller;

import com.example.api.domain.receivingitems.ReceivingItemService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("receiving-items")
@SecurityRequirement(name = "bearer-key")
public class ReceivingItemController {

    @Autowired
    private ReceivingItemService receivingItemService;

    @GetMapping("/{id}")
    public ResponseEntity detail(@PathVariable Long id) {
        var receivingItem = receivingItemService.show(id);

        return ResponseEntity.ok(receivingItem);
    }
}
