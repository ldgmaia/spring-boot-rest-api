package com.example.api.domain.receivingitems;


import com.example.api.repositories.InventoryItemRepository;

import java.time.LocalDateTime;

public record ReceivingItemAssessmentListDTO(
        Long id,
        String poNumber,
        Long receivingId,
        String description,
        String status,
        Long quantityOrdered,
        Long quantityAlreadyReceived,
        Long quantityAdded,
        String supplierName,
        LocalDateTime createdAt) {
    public ReceivingItemAssessmentListDTO(
            Long id,
            String poNumber,
            Long receivingId,
            String description,
            String status,
            Long quantityOrdered,
            Long quantityAlreadyReceived,
            Long quantityAdded,
            String supplierName,
            LocalDateTime createdAt) {
        this.id = id;
        this.poNumber = poNumber;
        this.receivingId = receivingId;
        this.description = description;
        this.status = status;
        this.quantityOrdered = quantityOrdered;
        this.quantityAlreadyReceived = quantityAlreadyReceived;
        this.quantityAdded = quantityAdded;
        this.supplierName = supplierName;
        this.createdAt = createdAt;
    }

    public ReceivingItemAssessmentListDTO(ReceivingItem receivingItem, InventoryItemRepository inventoryItemRepository) {
//        this.id = receivingItem.getId();
//        this.poNumber = receivingItem.getPurchaseOrderItem() != null ? receivingItem.getPurchaseOrderItem().getPurchaseOrder().getPoNumber() : null;
//        this.description = receivingItem.getDescription();
//        this.status = receivingItem.getStatus();
//        this.quantityOrdered = receivingItem.getPurchaseOrderItem() != null ? receivingItem.getPurchaseOrderItem().getQuantityOrdered() : null;
//        this.quantityAlreadyReceived = receivingItem.getQuantityAlreadyReceived();
//        this.supplierName = receivingItem.getReceiving().getSupplier().getName();
//        this.createdAt = receivingItem.getCreatedAt();
        this(
                receivingItem.getId(),
                receivingItem.getPurchaseOrderItem() != null ? receivingItem.getPurchaseOrderItem().getPurchaseOrder().getPoNumber() : null,
                receivingItem.getReceiving().getId(),
                receivingItem.getDescription(),
                receivingItem.getStatus(),
                receivingItem.getPurchaseOrderItem() != null ? receivingItem.getPurchaseOrderItem().getQuantityOrdered() : null,
                receivingItem.getQuantityAlreadyReceived(),
                inventoryItemRepository.countByReceivingItemId(receivingItem.getId()),  // Fetch quantityAdded
                receivingItem.getReceiving().getSupplier().getName(),
                receivingItem.getCreatedAt()
        );
    }
}
