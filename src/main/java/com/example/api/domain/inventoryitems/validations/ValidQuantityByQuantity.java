//package com.example.api.domain.inventoryitems.validations;
//
//import jakarta.validation.Constraint;
//import jakarta.validation.Payload;
//
//import java.lang.annotation.ElementType;
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//import java.lang.annotation.Target;
//
//@Target({ElementType.TYPE})
//@Retention(RetentionPolicy.RUNTIME)
//@Constraint(validatedBy = ByQuantityValidator.class)
//public @interface ValidQuantityByQuantity {
//    String message() default "Quantity must be greater than 0 when 'byQuantity' is true";
//
//    Class<?>[] groups() default {};
//
//    Class<? extends Payload>[] payload() default {};
//}
