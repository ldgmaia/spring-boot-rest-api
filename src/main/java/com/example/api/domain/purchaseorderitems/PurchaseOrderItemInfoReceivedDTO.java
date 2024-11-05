package com.example.api.domain.purchaseorderitems;

import com.example.api.domain.receivingitems.ReceivingItem;
import com.example.api.domain.receivingitems.ReceivingItemInfoDTO;

import java.math.BigDecimal;

public record PurchaseOrderItemInfoReceivedDTO(
        Long id,
        String name,
        String description,
        Long quantityOrdered,
        Long quantityAlreadyReceived,
        BigDecimal unitPrice,
        BigDecimal total,
        ReceivingItemInfoDTO receivingItems
) {
    public PurchaseOrderItemInfoReceivedDTO(PurchaseOrderItem poi, Long quantityAlreadyReceived, ReceivingItem receivingItem) {
        this(
                poi.getId(),
                poi.getName(),
                poi.getDescription(),
                poi.getQuantityOrdered(),
                quantityAlreadyReceived,
                poi.getUnitPrice(),
                poi.getTotal(),
                receivingItem != null ? new ReceivingItemInfoDTO(receivingItem) : null
        );
    }
}
