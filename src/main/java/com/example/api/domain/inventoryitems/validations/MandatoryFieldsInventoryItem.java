package com.example.api.domain.inventoryitems.validations;

import com.example.api.domain.ValidationException;
import com.example.api.domain.inventoryitems.InventoryRequestDTO;
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

        if (data.byQuantity() && data.quantity() == null) {
            throw new ValidationException("Serial number should be null due to item managed by quantity");
        }

        if (data.byQuantity()) {
            if (data.serialNumber() != null)
                throw new ValidationException("Serial number should be null due to item managed by quantity");
        } else if (data.quantity() != null)
            throw new ValidationException("Quantity should be null");

//        // Get the total number of items already added
//        Long totalAdded = inventoryRepository.countByReceivingItemId(data.receivingItemId());
//        // Fetch the total quantity received for this item
//        Long quantityReceived = receivingItemRepository.findQuantityReceivedById(data.receivingItemId());
//        // Calculate how many more can be added
//        Long itemsLeftToAdd = quantityReceived - totalAdded;
//
//        System.err.println("totalAdded " + totalAdded );
//        System.err.println("quantityReceived " + quantityReceived );
//        System.err.println("itemsLeftToAdd " + itemsLeftToAdd );
//
//        if (itemsLeftToAdd < 1 ) {
//            throw new ValidationException("No more items to add");
//        }
    }
}
