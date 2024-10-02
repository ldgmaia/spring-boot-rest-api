package com.example.api.domain.purchaseorderitems;

import java.math.BigDecimal;

public record PurchaseOrderItemInfoReceivedDTO(
        Long id,
        String name,
        String description,
        Long quantityOrdered,
        Long quantityReceived,
        BigDecimal unitPrice,
        BigDecimal total
) {
    public PurchaseOrderItemInfoReceivedDTO(PurchaseOrderItem poi, Long quantityReceived) {
        this(
                poi.getId(),
                poi.getName(),
                poi.getDescription(),
                poi.getQuantityOrdered(),
                quantityReceived,
                poi.getUnitPrice(),
                poi.getTotal()
        );
    }
}
