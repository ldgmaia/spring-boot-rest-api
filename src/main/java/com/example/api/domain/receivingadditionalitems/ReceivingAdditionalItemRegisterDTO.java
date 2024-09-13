package com.example.api.domain.receivingadditionalitems;

import com.example.api.domain.receivings.Receiving;
import com.example.api.domain.users.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReceivingAdditionalItemRegisterDTO(

        @NotNull
        Receiving receiving,

//        @NotNull
//        PurchaseOrderItem purchaseOrderItem,

        @NotBlank
        String description,

        @NotBlank
        Long quantityReceived,

        @NotBlank
        User createdBy

        //        @NotNull
//        Boolean receivableItem

) {

}