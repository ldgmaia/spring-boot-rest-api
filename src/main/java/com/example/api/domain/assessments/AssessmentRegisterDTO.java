package com.example.api.domain.assessments;

import com.example.api.domain.purchaseorderitems.PurchaseOrderItem;
import com.example.api.domain.receivings.Receiving;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AssessmentRegisterDTO(

        PurchaseOrderItem purchaseOrderItem,

        @NotNull
        Receiving receiving,

        @NotBlank
        String description,

        @NotBlank
        String status,

        @NotNull
        Long quantityReceived,

        @NotNull
        LocalDateTime createdAt
) {
}
