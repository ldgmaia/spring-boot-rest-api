package com.example.api.domain.receivingitems;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReceivingItemRequestDTO(

        Long purchaseOrderItemId,

        @NotBlank
        String description,

        Long quantityToReceive,

        @NotBlank
        Long quantity,

        @NotNull
        Boolean receivableItem,

        Boolean additionalItem
) {
    // Primary constructor
    public ReceivingItemRequestDTO(
            Long purchaseOrderItemId,
            String description,
            Long quantityToReceive,
            Long quantity,
            Boolean receivableItem,
            Boolean additionalItem) {
        this.purchaseOrderItemId = purchaseOrderItemId;
        this.description = description;
        this.quantityToReceive = quantityToReceive;
        this.quantity = quantity;
        this.receivableItem = receivableItem;
        this.additionalItem = additionalItem; // Set to null if not provided
    }

//        // Overloaded constructor with additionalItem defaulted to null
//        public ReceivingItemRequestDTO(
//                Long purchaseOrderItemId,
//                String description,
//                Long quantityToReceive,
//                Long quantity,
//                Boolean receivableItem) {
//                this(purchaseOrderItemId, description, quantityToReceive, quantity, receivableItem, null);
//        }
}
