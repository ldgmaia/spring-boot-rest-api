//package com.example.api.domain.inventoryitems.validations;
//
//import com.example.api.domain.inventoryitems.InventoryRequestDTO;
//import jakarta.validation.ConstraintValidator;
//import jakarta.validation.ConstraintValidatorContext;
//
//public class ByQuantityValidator implements ConstraintValidator<ValidQuantityByQuantity, InventoryRequestDTO> {
//
//    @Override
//    public boolean isValid(InventoryRequestDTO data, ConstraintValidatorContext context) {
//        if (data.byQuantity()) {
//            return data.quantity() != null && data.quantity() > 0;
//        }
//        // If byQuantity is false, no need to validate quantity
//        return true;
//    }
//}
