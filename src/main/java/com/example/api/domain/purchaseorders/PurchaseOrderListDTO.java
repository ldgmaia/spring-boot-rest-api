package com.example.api.domain.purchaseorders;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PurchaseOrderListDTO(
        Long id,
        String status,
        String poNumber,
        String currency,
        BigDecimal total,
//        String receivingStatus,
        LocalDateTime lastReceivedAt
) {
    public PurchaseOrderListDTO(PurchaseOrder purchaseOrder) {
        this(
                purchaseOrder.getId(),
                purchaseOrder.getStatus(),
                purchaseOrder.getPoNumber(),
                purchaseOrder.getCurrency(),
                purchaseOrder.getTotal(),
//                purchaseOrder.getReceivingStatus(),
                purchaseOrder.getLastReceivedAt()
        );
    }
}
