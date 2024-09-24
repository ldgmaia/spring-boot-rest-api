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

        @NotNull
        Boolean receivableItem,

        Boolean additionalItem
) {
    // Custom constructor for setting default value for additionalItem
    public ReceivingItemRequestDTO(ReceivingItem receivingItem) {
        // Set default value for additionalItem if not provided (i.e., null)
        this(
                receivingItem.getPurchaseOrderItem().getQboPurchaseOrderItemId(),
                receivingItem.getDescription(),
                receivingItem.getQuantityToReceive(),
                receivingItem.getQuantityReceived(),
                receivingItem.getReceivableItem(),
                receivingItem.getAdditionalItem() != null ? receivingItem.getAdditionalItem() : false);
    }
}
