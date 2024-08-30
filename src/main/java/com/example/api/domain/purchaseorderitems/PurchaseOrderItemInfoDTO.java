package com.example.api.domain.purchaseorderitems;

import java.math.BigDecimal;

public record PurchaseOrderItemInfoDTO(
        Long id,
        String name,
        String description,
        Long quantity,
        BigDecimal unitPrice,
        BigDecimal total
) {
    public PurchaseOrderItemInfoDTO(PurchaseOrderItem poi) {
        this(
                poi.getId(),
                poi.getName(),
                poi.getDescription(),
                poi.getQuantity(),
                poi.getUnitPrice(),
                poi.getTotal()
        );
    }
}


