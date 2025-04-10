package com.example.api.domain.inventoryitems;

import jakarta.validation.constraints.NotNull;

import java.util.List;


public record InventoryItemLookUpRequestDTO(
        @NotNull
        Long modelId,

        @NotNull
        List<Long> statusesIds,

        @NotNull
        List<String> fieldsTypes
) {
}
