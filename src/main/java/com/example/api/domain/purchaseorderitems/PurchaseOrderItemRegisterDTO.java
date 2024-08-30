package com.example.api.domain.purchaseorderitems;

import com.example.api.domain.purchaseorders.PurchaseOrder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PurchaseOrderItemRegisterDTO(
        @NotBlank
        String name,

        String description,

        @NotNull
        Long quantity,
        @NotNull
        BigDecimal unitPrice,
        @NotNull
        BigDecimal total,
        @NotNull
        Long qboItemId,
        @NotNull
        Long qboPurchaseOrderItemId,

        @NotNull
        PurchaseOrder purchaseOrder
) {
}
