package com.example.api.domain.inventory;

import com.example.api.domain.users.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record InventoryRegisterDTO(
        @NotNull Long id,
        @NotNull Long categoryId,
        @NotNull Long modelId,
        @NotNull Long mpnId,
        @NotNull Long conditionId,
        @NotNull Long receivingItemId,
        @NotNull User createdBy,
        @NotBlank String post,
        @NotNull Boolean byQuantity,  // Changed from @NotBlank to @NotNull
        Long quantity,
        String serialNumber
) {
}


