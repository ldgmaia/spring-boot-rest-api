package com.example.api.domain.purchaseorders;

import com.example.api.domain.purchaseorderitems.PurchaseOrderItemInfoReceivedDTO;
import com.example.api.domain.suppliers.Supplier;
import com.example.api.domain.suppliers.SupplierInfoDTO;

import java.time.LocalDateTime;
import java.util.List;

public record PurchaseOrderInfoReceivedDTO(
        Long id,
        String poNumber,
        String status,
        LocalDateTime lastReceivedAt,
        SupplierInfoDTO supplier,
        List<PurchaseOrderItemInfoReceivedDTO> items
) {
    public PurchaseOrderInfoReceivedDTO(PurchaseOrder purchaseOrder, Supplier supplier, List<PurchaseOrderItemInfoReceivedDTO> items) {
        this(
                purchaseOrder.getId(),
                purchaseOrder.getPoNumber(),
                purchaseOrder.getStatus(),
                purchaseOrder.getLastReceivedAt(),
                new SupplierInfoDTO(supplier),
                items
        );
    }
}
