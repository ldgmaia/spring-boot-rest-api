package com.example.api.domain.inventoryitems;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

//@ValidQuantityByQuantity
public record InventoryItemRequestDTO(
        @NotNull Long receivingItemId,
        @NotNull Long categoryId,
        @NotNull Long modelId,
        Long mpnId,
        @NotNull Long itemConditionId,
        @NotBlank String post,
        @NotNull Boolean byQuantity,
        Long quantity,
        String serialNumber,
        String type
) {
    public InventoryItemRequestDTO {
        post = post.toUpperCase();  // Ensure POST is uppercase during record creation
    }
}
