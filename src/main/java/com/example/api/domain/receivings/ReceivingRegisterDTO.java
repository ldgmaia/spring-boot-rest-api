package com.example.api.domain.receivings;

import com.example.api.domain.carriers.Carrier;
import com.example.api.domain.suppliers.Supplier;
import jakarta.validation.constraints.NotNull;

public record ReceivingRegisterDTO(

        String trackingLading,

        @NotNull
        ReceivingType type,

        @NotNull
        Long identifierId,

        @NotNull
        Supplier supplier,

//        @NotNull
//        PurchaseOrder purchaseOrderId,

        Carrier carrier,

        String notes

//        List<Long> pictureIds,

//        @NotNull
//        List<ReceivingItemsDTO> receivingItems,
//
//        @NotNull
//        List<AdditionAllItemsDTO> additionalItems

) {

}

