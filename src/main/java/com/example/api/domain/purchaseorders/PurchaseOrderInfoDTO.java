package com.example.api.domain.purchaseorders;

import com.example.api.domain.suppliers.Supplier;
import com.example.api.domain.suppliers.SupplierInfoDTO;

import java.time.LocalDateTime;

public record PurchaseOrderInfoDTO(
        Long id,
        String poNumber,
        String status,
        LocalDateTime lastReceivedAt,
        SupplierInfoDTO supplier

) {

//    public PurchaseOrderInfoDTO(PurchaseOrder purchaseOrder, Supplier supplier, Boolean enabled) {
//        this(purchaseOrder.getId(),
//                purchaseOrder.getPoNumber(),
//                purchaseOrder.getStatus(),
//                purchaseOrder.getLastReceivedAt(),
//                supplier.getName()
//
//        );
//    }

//    public PurchaseOrderInfoDTO(PurchaseOrder purchaseOrder) {
//        this(purchaseOrder.getId(),
//                purchaseOrder.getPoNumber(),
//                purchaseOrder.getStatus(),
//                purchaseOrder.getLastReceivedAt(),
//                null
//        );
//    }

    public PurchaseOrderInfoDTO(PurchaseOrder purchaseOrder, Supplier supplier) {
        this(
                purchaseOrder.getId(),
                purchaseOrder.getPoNumber(),
                purchaseOrder.getStatus(),
                purchaseOrder.getLastReceivedAt(),
                new SupplierInfoDTO(supplier)
        );
    }


}


