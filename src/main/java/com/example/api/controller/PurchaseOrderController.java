package com.example.api.controller;

import com.example.api.domain.purchaseorders.PurchaseOrderListDTO;
import com.example.api.domain.purchaseorders.PurchaseOrderService;
import com.example.api.repositories.PurchaseOrderRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("purchase-orders")
@SecurityRequirement(name = "bearer-key")
public class PurchaseOrderController {

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

//    @PostMapping
//    @Transactional
//    public ResponseEntity register(@RequestBody @Valid PurchaseOrderRequestDTO data, UriComponentsBuilder uriBuilder) {
//
//        var purchaseOrder = purchaseOrderService.register(data);
//        var uri = uriBuilder.path("/purchase-orders/{id}").buildAndExpand(purchaseOrder.id()).toUri();
//
//        return ResponseEntity.created(uri).body(purchaseOrder);
//    }

    @GetMapping
    public ResponseEntity<Page<PurchaseOrderListDTO>> list(@PageableDefault(size = 100, page = 0, sort = {"poNumber"}) Pageable pagination, @RequestHeader HttpHeaders headers) {
        var page = purchaseOrderRepository.findAll(pagination).map(PurchaseOrderListDTO::new);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity detail(@PathVariable Long id) {
        var purchaseOrder = purchaseOrderService.show(id);
        return ResponseEntity.ok(purchaseOrder);
    }

    @GetMapping("/getSupplier/{id}")
    public ResponseEntity<?> getSupplier(@PathVariable(required = false) String id) {
        // Validate if the id is a valid Long
        try {
            Long parsedId = Long.valueOf(id); // Try converting id to Long

            // Call the repository method with the parsed Long id
            var supplier = purchaseOrderRepository.getSupplier(parsedId);

            // If the supplier list is empty, return a 404 response
            if (supplier == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No supplier found for Purchase Order ID: " + parsedId);
            }

            // If the supplier is found, return the response
            return ResponseEntity.ok(supplier);

        } catch (NumberFormatException e) {
            // Handle the case where the id is not a valid Long
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid Purchase Order ID. Please provide a valid number.");
        }
    }

    @GetMapping("/list/open-purchase-orders")
    public ResponseEntity getNonFullyReceivedPurchaseOrders() {
        var purchaseOrderInfoDTOs = purchaseOrderRepository.findAllByStatusNot("Fully Received");
        return ResponseEntity.ok(purchaseOrderInfoDTOs);
    }
}
