package com.example.api.domain.inventory.validations;

import com.example.api.domain.ValidationException;
import com.example.api.domain.inventory.InventoryRequestDTO;
import com.example.api.repositories.CategoryRepository;
import com.example.api.repositories.ModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MandatoryFieldsInventoryItem implements InventoryValidator {

    @Autowired
    private ModelRepository modelRepository = null;

    @Autowired
    private CategoryRepository categoryRepository = null;

    @Override
    public void validate(InventoryRequestDTO data) {

        // Validation 1: Check if model needs MPN
        var model = modelRepository.findById(data.modelId())
                .orElseThrow(() -> new ValidationException("Model not found"));

        if (model.getNeedsMpn() && data.mpnId() == null) { //need mpn is 1
            throw new ValidationException("MPN is required for this model.");
        }

        // Validation 2 & 3: Check if category needs or does not need serial number
        var category = categoryRepository.findById(data.categoryId())
                .orElseThrow(() -> new ValidationException("Category not found"));

        if (!category.getNeedsSerialNumber()) {// Category does not need serial number
            if (data.quantity() == null && data.serialNumber() == null) {
                throw new ValidationException("Either quantity or serial number must be provided.");
            }
        } else {// Category needs serial number
            if (data.serialNumber() == null) {
                throw new ValidationException("Serial number is required for this category.");
            }
        }
    }
}
