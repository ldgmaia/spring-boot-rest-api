package com.example.api.domain.receivingitems;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReceivingItemRequestDTO(

        @NotNull
        Long purchaseOrderItemId,

        @NotBlank
        String description,

        @NotBlank
        Long quantityToReceive,

        @NotBlank
        Long quantityReceived,

        @NotBlank
        Boolean receivableItem

//        @NotBlank
//        Long createdBy


) {

}