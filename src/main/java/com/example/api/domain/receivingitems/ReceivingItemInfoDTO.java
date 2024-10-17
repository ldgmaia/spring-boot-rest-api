package com.example.api.domain.receivingitems;

import com.example.api.domain.purchaseorderitems.PurchaseOrderItemInfoDTO;
import com.example.api.domain.purchaseorders.PurchaseOrderInfoDetailsDTO;
import com.example.api.domain.suppliers.SupplierInfoDTO;

import java.time.LocalDateTime;

public record ReceivingItemInfoDTO(
        Long id,
        Long purchaseOrderItemId,
        String description,
        Long quantityToReceive,
        Long quantityReceived,
        Long receivingId,
        String createdBy,
        String status,
        LocalDateTime updatedAt,
        PurchaseOrderItemInfoDTO purchaseOrderItem,
        PurchaseOrderInfoDetailsDTO purchaseOrder,
        SupplierInfoDTO supplier
) {
    public ReceivingItemInfoDTO(ReceivingItem receivingItem) {
        this(
                receivingItem.getId(),
                receivingItem.getPurchaseOrderItem() != null ? receivingItem.getPurchaseOrderItem().getId() : null,
                receivingItem.getDescription(),
                receivingItem.getQuantityToReceive(),
                receivingItem.getQuantityReceived(),
                receivingItem.getReceiving().getId(),
                receivingItem.getCreatedBy().getUsername(),
                receivingItem.getStatus(),
                receivingItem.getUpdatedAt(),
                receivingItem.getPurchaseOrderItem() != null ? new PurchaseOrderItemInfoDTO(receivingItem.getPurchaseOrderItem()) : null,
                receivingItem.getPurchaseOrderItem() != null ? new PurchaseOrderInfoDetailsDTO(receivingItem.getPurchaseOrderItem().getPurchaseOrder()) : null,
                new SupplierInfoDTO(receivingItem.getReceiving().getSupplier())
        );
    }
}
