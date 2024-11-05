package com.example.api.domain.receivingitems;


import java.time.LocalDateTime;

public record ReceivingItemAssessmentListDTO(
        Long id,
        String poNumber,
        String description,
        String status,
        Long quantityOrdered,
        Long quantityAlreadyReceived,
        String supplierName,
        LocalDateTime createdAt) {
    public ReceivingItemAssessmentListDTO(
            Long id,
            String poNumber,
            String description,
            String status,
            Long quantityOrdered,
            Long quantityAlreadyReceived,
            String supplierName,
            LocalDateTime createdAt) {
        this.id = id;
        this.poNumber = poNumber;
        this.description = description;
        this.status = status;
        this.quantityOrdered = quantityOrdered;
        this.quantityAlreadyReceived = quantityAlreadyReceived;
        this.supplierName = supplierName;
        this.createdAt = createdAt;
    }
}
