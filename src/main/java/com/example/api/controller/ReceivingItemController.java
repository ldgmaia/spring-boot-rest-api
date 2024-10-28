package com.example.api.controller;

import com.example.api.domain.receivingitems.ReceivingItemService;
import com.example.api.domain.receivings.ReceivingAssessmentListDTO;
import com.example.api.repositories.ReceivingItemRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
    public ResponseEntity detail(@PathVariable Long id) {
        var receivingItem = receivingItemService.show(id);

        return ResponseEntity.ok(receivingItem);
    }

    @GetMapping
    public ResponseEntity listReceivingsByStatus(@RequestParam(required = false, defaultValue = "Pending assessment") String[] status, @PageableDefault(size = 100, page = 0, sort = {"createdAt"}) Pageable pagination) {
        Page<ReceivingAssessmentListDTO> receivingsByStatus = receivingItemRepository.findReceivingsByStatus(status, pagination);
        return ResponseEntity.ok(receivingsByStatus);
    }

    @GetMapping("/by-po-number/{poNumber}")
    public ResponseEntity<List<ReceivingAssessmentListDTO>> listReceivingsByPurchaseOrder(@RequestParam(required = false, defaultValue = "Pending assessment") String[] status, @PathVariable String poNumber) {
        List<ReceivingAssessmentListDTO> receivingItemsByPoNumber = receivingItemRepository.findReceivedItemsByPurchaseOrderNumber(status, poNumber);

        return ResponseEntity.ok(receivingItemsByPoNumber);
    }

    @GetMapping("/by-serial-number/{serialNumber}")
    public ResponseEntity<List<ReceivingAssessmentListDTO>> listReceivingsByserialNumber(@PathVariable String serialNumber) {
        List<ReceivingAssessmentListDTO> receivingItemsBySerialNumber = receivingItemRepository.findReceivedItemsBySerialNumber(serialNumber);

        return ResponseEntity.ok(receivingItemsBySerialNumber);
    }

    @GetMapping("/by-receiving-id/{receivingId}")
    public ResponseEntity<List<ReceivingAssessmentListDTO>> listReceivingsByReceivingId(@RequestParam(required = false, defaultValue = "Pending assessment") String[] status, @PathVariable String receivingId) {
        List<ReceivingAssessmentListDTO> receivingItemsReceivingId = receivingItemRepository.findReceivedItemsByReceivingId(status, receivingId);
        return ResponseEntity.ok(receivingItemsReceivingId);
    }

    @GetMapping("/by-description/{description}")
    public ResponseEntity<List<ReceivingAssessmentListDTO>> listReceivingsByDescription(@RequestParam(required = false, defaultValue = "Pending assessment") String[] status, @PathVariable String description, @PageableDefault(size = 100, page = 0, sort = {"createdAt"}) Pageable pagination) {
        List<ReceivingAssessmentListDTO> receivingItemsByDescription = receivingItemRepository.findReceivedItemsByDescription(status, description);
        return ResponseEntity.ok(receivingItemsByDescription);
    }


    @GetMapping("/by-tracking-lading/{trackingLading}")
    public ResponseEntity<List<ReceivingAssessmentListDTO>> listReceivingsByTrakingLading(@RequestParam(required = false, defaultValue = "Pending assessment") String[] status, @PathVariable String trackingLading) {
        List<ReceivingAssessmentListDTO> receivingsItemsByTrakingLading = receivingItemRepository.findReceivedItemsByTrackinglading(status, trackingLading);
        return ResponseEntity.ok(receivingsItemsByTrakingLading);
    }

    @GetMapping("/by-supplier-id/{supplierId}")
    public ResponseEntity<List<ReceivingAssessmentListDTO>> listReceivingsBySupplierId(@RequestParam(required = false, defaultValue = "Pending assessment") String[] status, @PathVariable String supplierId, @PageableDefault(size = 100, page = 0, sort = {"createdAt"}) Pageable pagination) {
        List<ReceivingAssessmentListDTO> receivingsItemsBySupplierId = receivingItemRepository.findReceivedItemsBySupplierId(status, supplierId);
        return ResponseEntity.ok(receivingsItemsBySupplierId);
    }

}
