package com.example.api.domain.inventoryitems.validations;

import com.example.api.domain.ValidationException;
import com.example.api.domain.inventoryitems.InventoryItemRequestDTO;
import com.example.api.repositories.CategoryRepository;
import com.example.api.repositories.ModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MandatoryFieldsInventoryItem implements InventoryValidator {

    @Autowired
    private ModelRepository modelRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public void validate(InventoryItemRequestDTO data) {

        // Validation 1: Check if model needs MPN
        var model = modelRepository.findById(data.modelId())
                .orElseThrow(() -> new ValidationException("Model not found"));

        if (model.getNeedsMpn() && data.mpnId() == null) { //need mpn is 1
            throw new ValidationException("MPN is required for this model.");
        }

        // Validation 3: Validate based on the byQuantity flag
        if (data.byQuantity()) {
            // Case 1: If managed by quantity
            if (data.quantity() == null || data.quantity() <= 0) { // Quantity must be a positive number
                throw new ValidationException("Quantity must be a positive number when 'byQuantity' is true.");
            }
            if (data.serialNumber() != null) { // Serial number must be null when managed by quantity
                throw new ValidationException("Serial number must be null when 'byQuantity' is true.");
            }
        } else {
            // Case 2: If managed by serial number
            if (data.quantity() != null) { // Quantity must be null when managed by serial number
                throw new ValidationException("Quantity must be null when 'byQuantity' is false.");
            }
            if (data.serialNumber() == null || data.serialNumber().isEmpty()) { // Serial number must not be null or empty
                throw new ValidationException("Serial number is required when 'byQuantity' is false.");
            }
        }

        // Validation 4: Ensure 'type' is not null
        if (data.type() == null) {
            throw new ValidationException("Type is required.");
        }

        // Validation 4: Check if byQuantity and related fields are correctly set
        if (data.byQuantity()) {
            if (data.quantity() == null) {
                throw new ValidationException("Quantity is required when 'byQuantity' is true.");
            }
            if (data.serialNumber() != null) {
                throw new ValidationException("Serial number must be null when 'byQuantity' is true.");
            }
        } else { // If byQuantity is false
            if (data.quantity() != null) {
                throw new ValidationException("Quantity must be null when 'byQuantity' is false.");
            }
            if (data.serialNumber() == null) {
                throw new ValidationException("Serial number is required when 'byQuantity' is false.");
            }
        }

    }
}

// Orginal validations

// Validation 2 & 3: Check if category needs or does not need serial number
//        var category = categoryRepository.findById(data.categoryId())
//                .orElseThrow(() -> new ValidationException("Category not found"));
//
//        if (!category.getNeedsSerialNumber()) {// Category does not need serial number
//            if (data.byQuantity() && data.quantity() == null && data.serialNumber() == null) {
//                throw new ValidationException("Either quantity or serial number must be provided.");
//            }
//        } else {// Category needs serial number
//            if (data.serialNumber() == null) {
//                throw new ValidationException("Serial number is required for this category.");
//            }
//        }


//        // Validation 2: Check if category needs serial number
//        var category = categoryRepository.findById(data.categoryId())
//                .orElseThrow(() -> new ValidationException("Category not found"));
//
//
//        // Validation 3: If category does not need serial number, either quantity or serial number must be provided
//        if (!category.getNeedsSerialNumber()) { // Category does not need serial number
//            if (data.byQuantity() && data.quantity() == null) {
//                throw new ValidationException("Quantity must be provided when managed by quantity.");
//            }
//        } else { // Category needs serial number
//            if (data.serialNumber() == null) {
//                throw new ValidationException("Serial number is required for this category.");
//            }
//        }

//        if (data.byQuantity() && data.quantity() == null) {
//            throw new ValidationException("Serial number should be null due to item managed by quantity");
//        }
//
//        if (data.byQuantity()) {
//            if (data.serialNumber() != null)
//                throw new ValidationException("Serial number should be null due to item managed by quantity");
//        } else if (data.quantity() != null)
//            throw new ValidationException("Quantity should be null");

//        // Validation 5: Ensure 'type' is not null
//        if (data.type() == null) {
//            throw new ValidationException("Type is required.");
//        }
