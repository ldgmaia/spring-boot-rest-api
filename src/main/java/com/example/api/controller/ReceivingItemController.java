package com.example.api.controller;

import com.example.api.domain.receivingitems.ReceivingItemAssessmentListDTO;
import com.example.api.domain.receivingitems.ReceivingItemInfoDTO;
import com.example.api.domain.receivingitems.ReceivingItemListByRequestDTO;
import com.example.api.domain.receivingitems.ReceivingItemService;
import com.example.api.repositories.ReceivingItemRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("receiving-items")
@SecurityRequirement(name = "bearer-key")
public class ReceivingItemController {

    @Autowired
    private ReceivingItemService receivingItemService;

    @Autowired
    private ReceivingItemRepository receivingItemRepository;

    @GetMapping("/{id}")
    public ResponseEntity<ReceivingItemInfoDTO> detail(@PathVariable Long id) {
        var receivingItem = receivingItemService.show(id);

        return ResponseEntity.ok(receivingItem);
    }

    @GetMapping
    public ResponseEntity<Page<ReceivingItemAssessmentListDTO>> listReceivingsByStatus(@RequestParam(required = false, defaultValue = "Pending assessment") String[] status, @PageableDefault(size = 100, page = 0, sort = {"createdAt"}) Pageable pagination) {
//        Arrays.stream(status).forEach(System.out::println);
//        System.out.println("status 1 -> " + status[0]);
//        System.out.println("status 2 -> " + status[1]);
        Page<ReceivingItemAssessmentListDTO> receivingsByStatus = receivingItemRepository.listPagedReceivingsByStatus(pagination, status);
        return ResponseEntity.ok(receivingsByStatus);
    }

    @PutMapping("/list/by-criteria")
    public ResponseEntity<List<ReceivingItemAssessmentListDTO>> listReceivingsByCriteria(@RequestBody @Valid ReceivingItemListByRequestDTO data) {
        var receivingList = receivingItemService.list(data);
        return ResponseEntity.ok(receivingList);
    }
}
