package com.example.api.domain.receivingitems;

public record ReceivingItemsInfoDTO(
        Long purchaseOrderItemId,
        String description,
        Long quantityToReceive,
        Long quantityReceived,
        Long receivingId,
        String createdBy

) {
    public ReceivingItemsInfoDTO(ReceivingItemsInfoDTO receivingItemsInfoDTO) {
        this(
                receivingItemsInfoDTO.purchaseOrderItemId(),
                receivingItemsInfoDTO.description(),
                receivingItemsInfoDTO.quantityToReceive(),
                receivingItemsInfoDTO.quantityReceived(),
                receivingItemsInfoDTO.receivingId(),
                receivingItemsInfoDTO.createdBy()
        );

    }

}
