package com.example.api.domain.inventoryitems.validations;

import com.example.api.domain.inventoryitems.InventoryRequestDTO;

public interface InventoryValidator {

    void validate(InventoryRequestDTO data);
}
