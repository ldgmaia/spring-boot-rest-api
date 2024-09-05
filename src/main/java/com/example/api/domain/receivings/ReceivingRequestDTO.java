package com.example.api.domain.receivings;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ReceivingRequestDTO(

        @NotBlank
        String trackingCode,

        @NotNull
        ReceivingType type,

        @NotNull
        String identifier,

        @NotNull
        Long supplierId,

        @NotNull
        Long purchaseOrderId,

        @NotNull
        Carriers carrier,

        String notes,

        List<Long> pictureIds,

        @NotNull
        List<ReceivingItemsDTO> receivingItems,
        @NotNull
        List<AdditionalItemsDTO> additionalItems
) {

}

