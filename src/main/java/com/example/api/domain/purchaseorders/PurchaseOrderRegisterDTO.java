package com.example.api.domain.purchaseorders;

import com.example.api.domain.categories.Category;
import jakarta.validation.constraints.NotBlank;

public record PurchaseOrderRegisterDTO(

        @NotBlank
        String name,

        String description,
        String identifier,
        String status,
        Boolean needsMpn,

        @NotBlank
        Category category
) {
}
