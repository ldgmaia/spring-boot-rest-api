//package com.example.api.domain.purchaseorders;
//
//import com.example.api.domain.modelfieldsvalues.ModelFieldValueRequestDTO;
//import com.example.api.domain.sections.SectionUpdateDTO;
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.NotNull;
//
//import java.util.List;
//
//public record PurchaseOrderUpdateDTO(
//
//        @NotBlank
//        String name,
//
//        String description,
//        String identifier,
//
//        @NotNull
//        Long categoryId,
//
//        Boolean needsMpn,
//
//        List<ModelFieldValueRequestDTO> modelFieldsValues,
//
//        List<SectionUpdateDTO> sections
//) {
//}
