package com.example.api.domain.inventoryitems;

import java.util.List;
import java.util.Map;


public record InventoryItemLookUpResponseDTO(
        List<Map<String, List<String>>> columns,
        List<Map<String, List<String>>> data
) {
}
