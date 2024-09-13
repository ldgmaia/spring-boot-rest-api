package com.example.api.domain.receivingadditionalitems;

public record ReceivingAdditionalItemsInfoDTO(
        Long purchaseOrderAdditionalItemId,
        String description,
        Long quantityReceived,
        Long receivingId,
        String createdBy

) {
    public ReceivingAdditionalItemsInfoDTO(ReceivingAdditionalItemsInfoDTO receivingAdditionalItemsInfoDTO) {
        this(
                receivingAdditionalItemsInfoDTO.purchaseOrderAdditionalItemId(),
                receivingAdditionalItemsInfoDTO.description(),
                receivingAdditionalItemsInfoDTO.quantityReceived(),
                receivingAdditionalItemsInfoDTO.receivingId(),
                receivingAdditionalItemsInfoDTO.createdBy()
        );

    }

}