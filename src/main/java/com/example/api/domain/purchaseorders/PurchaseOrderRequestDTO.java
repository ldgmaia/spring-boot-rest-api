package com.example.api.domain.purchaseorders;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PurchaseOrderRequestDTO(

        @NotBlank
        String qbo_id,

        @NotBlank
        String status,

        @NotBlank
        String po_number,

        @NotBlank
        String currency,

        @NotBlank
        String total,

        @NotBlank
        String qbo_created_at,

        @NotBlank
        String qbo_updated_at,

        @NotNull
        Long supplier_id
) {
}
