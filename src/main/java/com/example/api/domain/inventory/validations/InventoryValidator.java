package com.example.api.domain.inventory.validations;

import com.example.api.domain.inventory.InventoryRequestDTO;

public interface InventoryValidator {

    void validate(InventoryRequestDTO data);
}
