package com.example.api.domain.assessments;


import java.time.LocalDateTime;

public record PendingAssessmentListDTO(
        Long id,
        String poNumber,
        String description,
        String status,
        Long quantityOrdered,
        Long quantityReceived,
        String supplierName,
        LocalDateTime createdAt) {
    public PendingAssessmentListDTO(
            Long id,
            String poNumber,
            String description,
            String status,
            Long quantityOrdered,
            Long quantityReceived,
            String supplierName,
            LocalDateTime createdAt) {
        this.id = id;
        this.poNumber = poNumber;
        this.description = description;
        this.status = status;
        this.quantityOrdered = quantityOrdered;
        this.quantityReceived = quantityReceived;
        this.supplierName = supplierName;
        this.createdAt = createdAt;
    }
}
