package com.example.api.domain.purchaseorders;

import com.example.api.domain.purchaseorderitems.PurchaseOrderItemInfoDTO;
import com.example.api.domain.suppliers.Supplier;
import com.example.api.domain.suppliers.SupplierInfoDTO;

import java.time.LocalDateTime;
import java.util.List;

public record PurchaseOrderInfoDTO(
        Long id,
        String poNumber,
        String status,
        LocalDateTime lastReceivedAt,
        SupplierInfoDTO supplier,
        List<PurchaseOrderItemInfoDTO> items
) {
    public PurchaseOrderInfoDTO(PurchaseOrder purchaseOrder, Supplier supplier, List<PurchaseOrderItemInfoDTO> items) {
        this(
                purchaseOrder.getId(),
                purchaseOrder.getPoNumber(),
                purchaseOrder.getStatus(),
                purchaseOrder.getLastReceivedAt(),
                new SupplierInfoDTO(supplier),
                items
        );
    }

    public PurchaseOrderInfoDTO(PurchaseOrder purchaseOrder) {
        this(
                purchaseOrder.getId(),
                purchaseOrder.getPoNumber(),
                purchaseOrder.getStatus(),
                purchaseOrder.getLastReceivedAt(),
                null,
                null
        );
    }
}
