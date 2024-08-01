package com.example.api.domain.purchaseorders;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PurchaseOrderRegisterDTO(

        @NotBlank
        String status,
        String poNumber,
        String currency,
        BigDecimal total,
        Long qboId,
        LocalDateTime qboCreatedAt,
        LocalDateTime qboUpdatedAt,
        Long suppliersId,
        String receivingStatus,
        String watchingPo
) {
}
