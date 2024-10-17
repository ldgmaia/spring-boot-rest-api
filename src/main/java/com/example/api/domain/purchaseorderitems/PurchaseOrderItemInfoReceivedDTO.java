package com.example.api.domain.purchaseorderitems;

import com.example.api.domain.receivingitems.ReceivingItem;
import com.example.api.domain.receivingitems.ReceivingItemInfoDTO;

import java.math.BigDecimal;

public record PurchaseOrderItemInfoReceivedDTO(
        Long id,
        String name,
        String description,
        Long quantityOrdered,
        Long quantityReceived,
        BigDecimal unitPrice,
        BigDecimal total,
        ReceivingItemInfoDTO receivingItems
) {
    public PurchaseOrderItemInfoReceivedDTO(PurchaseOrderItem poi, Long quantityReceived, ReceivingItem receivingItem) {
        this(
                poi.getId(),
                poi.getName(),
                poi.getDescription(),
                poi.getQuantityOrdered(),
                quantityReceived,
                poi.getUnitPrice(),
                poi.getTotal(),
                receivingItem != null ? new ReceivingItemInfoDTO(receivingItem) : null
        );
    }
}
