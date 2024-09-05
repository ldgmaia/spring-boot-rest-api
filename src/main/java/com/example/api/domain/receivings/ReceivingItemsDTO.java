package com.example.api.domain.receivings;

public record ReceivingItemsDTO(
        Long purchaseOrderItemId,
        Long quantityReceived,
        Boolean receivableItem
) {
    public ReceivingItemsDTO(ReceivingItemsDTO receivingItemsDTO) {
        this(
                receivingItemsDTO.purchaseOrderItemId(),
                receivingItemsDTO.quantityReceived(),
                receivingItemsDTO.receivableItem());
    }
}