//package com.example.api.domain.inventoryitems;
//
//import com.example.api.domain.locations.Location;
//import com.example.api.domain.users.User;
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.NotNull;
//
//import java.math.BigDecimal;
//
//public record InventoryRegisterDTO(
//        @NotNull Long categoryId,
//        @NotNull Long modelId,
//        Long mpnId,
//        @NotNull Long itemConditionId,
//        @NotNull Long receivingItemId,
//        @NotNull Location location,
//        @NotNull User createdBy,
//        @NotBlank String post,
//        String serialNumber,
//        String rbid,
//        String type,
//        BigDecimal cost
//) {
//}
