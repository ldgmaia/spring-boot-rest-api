package com.example.api.domain.receivingitems;

import java.time.LocalDateTime;

public record ReceivingItemsInfoDTO(
        Long id,
        Long purchaseOrderItemId,
        String description,
        Long quantityToReceive,
        Long quantityReceived,
        Long receivingId,
        String createdBy,
        String status,
        LocalDateTime updatedAt
) {
    public ReceivingItemsInfoDTO(ReceivingItem receivingItem) {
        this(
                receivingItem.getId(),
                receivingItem.getPurchaseOrderItem() != null ? receivingItem.getPurchaseOrderItem().getId() : null,
                receivingItem.getDescription(),
                receivingItem.getQuantityToReceive(),
                receivingItem.getQuantityReceived(),
                receivingItem.getReceiving().getId(),
                receivingItem.getCreatedBy().getUsername(),
                receivingItem.getStatus(),
                receivingItem.getUpdatedAt()
        );
    }
}
