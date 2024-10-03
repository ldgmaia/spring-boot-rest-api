package com.example.api.domain.purchaseorders;

import com.example.api.domain.suppliers.Supplier;
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
        Supplier supplier,
//        String receivingStatus,
        String watchingPo
) {
}
