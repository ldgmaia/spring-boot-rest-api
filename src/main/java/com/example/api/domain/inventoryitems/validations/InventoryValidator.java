package com.example.api.domain.inventoryitems.validations;

import com.example.api.domain.inventoryitems.InventoryItemRequestDTO;

public interface InventoryValidator {

    void validate(InventoryItemRequestDTO data);
}
