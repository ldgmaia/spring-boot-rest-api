package com.example.api.domain.inventory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record InventoryRequestDTO(
        @NotNull Long inventoryId,
        @NotNull Long receivingItemId,
        @NotNull Long categoryId,
        @NotNull Long modelId,
        @NotNull Long mpnId,
        @NotNull Long conditionId,
        @NotBlank String post,
        @NotNull Boolean byQuantity,  // Changed from @NotBlank to @NotNull
        Long quantity,
        String serialNumber
) {
}


