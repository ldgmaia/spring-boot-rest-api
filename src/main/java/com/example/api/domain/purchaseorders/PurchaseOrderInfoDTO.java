package com.example.api.domain.purchaseorders;

import java.time.LocalDateTime;

public record PurchaseOrderInfoDTO(
        Long id,
        String poNumber,
        String status,
        LocalDateTime lastReceivedAt,
        String supplier

//        LocalDateTime qboCreatedAt,
//        String currency,
//        BigDecimal total,
//
//
//
//        Long qbo_id,
//        LocalDateTime qboUpdatedAt,
//        Long suppliers_id,
//        String received,
//        String watching_po,
//        LocalDateTime createdAt,
//        LocalDateTime updatedAt
) {
//    public PurchaseOrderInfoDTO(PurchaseOrder po) {
//        this(
//                po.getId(),
//                po.getStatus()
//        );
//    }
}


