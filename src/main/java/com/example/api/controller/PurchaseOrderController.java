package com.example.api.controller;

import com.example.api.domain.purchaseorders.PurchaseOrderInfoDTO;
import com.example.api.domain.purchaseorders.PurchaseOrderListDTO;
import com.example.api.domain.purchaseorders.PurchaseOrderRequestDTO;
import com.example.api.domain.purchaseorders.PurchaseOrderService;
import com.example.api.repositories.PurchaseOrderRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("purchase-orders")
@SecurityRequirement(name = "bearer-key")
public class PurchaseOrderController {

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;


    private PurchaseOrderInfoDTO purchaseOrderInfoDTO;

    @PostMapping
    @Transactional
    public ResponseEntity register(@RequestBody @Valid PurchaseOrderRequestDTO data, UriComponentsBuilder uriBuilder) {

        var purchaseOrder = purchaseOrderService.register(data);
        var uri = uriBuilder.path("/purchase-orders/{id}").buildAndExpand(purchaseOrder.id()).toUri();

        return ResponseEntity.created(uri).body(purchaseOrder);
    }


    @GetMapping
    public ResponseEntity<Page<PurchaseOrderListDTO>> list(HttpServletRequest request, @PageableDefault(size = 100, page = 0, sort = {"poNumber"}) Pageable pagination, @RequestHeader HttpHeaders headers) {
        var page = purchaseOrderRepository.findAll(pagination).map(PurchaseOrderListDTO::new);
        return ResponseEntity.ok(page);
    }


    @GetMapping("/{id}")
    public ResponseEntity detail(@PathVariable Long id) {

        var purchaseOrder = purchaseOrderService.show(id);
        return ResponseEntity.ok(purchaseOrder);

    }
}
