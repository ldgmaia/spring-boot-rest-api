package com.example.api.domain.inventoryitems;

import com.example.api.domain.users.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record InventoryRegisterDTO(
        @NotNull Long id,
        @NotNull Long categoryId,
        @NotNull Long modelId,
        @NotNull Long mpnId,
        @NotNull Long itemConditionsId,
        @NotNull Long receivingItemId,
        @NotNull Long locationId,
        @NotNull User createdBy,
        @NotBlank String post,
        String serialNumber,
        String rbid
) {
}
