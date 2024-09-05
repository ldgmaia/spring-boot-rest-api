package com.example.api.domain.receivings;

import com.example.api.domain.purchaseorders.PurchaseOrder;
import com.example.api.domain.suppliers.Supplier;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReceivingRegisterDTO(

        @NotBlank
        String trackingLading,

        @NotNull
        ReceivingType type,

        @NotNull
        String identifier,

        @NotNull
        Supplier supplierId,

        @NotNull
        PurchaseOrder purchaseOrderId,

        @NotNull
        Carriers carrier,

        String notes

//        List<Long> pictureIds,

//        @NotNull
//        List<ReceivingItemsDTO> receivingItems,
//
//        @NotNull
//        List<AdditionAllItemsDTO> additionalItems

) {

}

