package com.example.api.domain.receivingitems;

import com.example.api.domain.purchaseorderitems.PurchaseOrderItem;
import com.example.api.domain.receivings.Receiving;
import com.example.api.domain.users.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReceivingItemRegisterDTO(

        Receiving receiving,

        PurchaseOrderItem purchaseOrderItem,

        String description,

        Long quantityToReceive,

        @NotBlank
        Long quantityReceived,

        @NotBlank
        User createdBy,

        @NotNull
        Boolean receivableItem,

        @NotNull
        Boolean additionalItem

) {
}