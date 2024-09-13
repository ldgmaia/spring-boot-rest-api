package com.example.api.domain.receivingitems;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReceivingItemRequestDTO(

        Long purchaseOrderItemId,

        @NotBlank
        String description,

        Long quantityToReceive,

        @NotBlank
        Long quantityReceived,

        @NotBlank
        Boolean receivableItem,

        @NotNull
        Boolean additionalItem
) {
}