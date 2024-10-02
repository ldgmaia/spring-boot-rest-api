package com.example.api.domain.inventoryitems;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

//@ValidQuantityByQuantity
public record InventoryRequestDTO(
        @NotNull Long inventoryId,
        @NotNull Long receivingItemId,
        @NotNull Long categoryId,
        @NotNull Long modelId,
        @NotNull Long mpnId,
        @NotNull Long itemConditionsId,
        @NotBlank String post,
        @NotNull Boolean byQuantity,
        @NotNull Long locationId,
        Long quantity,
        String serialNumber
) {
    public InventoryRequestDTO {
        post = post.toUpperCase();  // Ensure POST is uppercase during record creation
    }
}
