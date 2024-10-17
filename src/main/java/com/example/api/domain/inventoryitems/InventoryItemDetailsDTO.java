//package com.example.api.domain.inventoryitems;
//
//import java.time.LocalDateTime;
//
//public record InventoryItemDetailsDTO(
//        String mpnName,
//        String serialNumber,
//        String conditionName,
//        String status,
//        String locationName,
//        LocalDateTime updatedAt,
//        String post,
//        Long rbid
//) {
//
//    public InventoryItemDetailsDTO(InventoryItemDetailsDTO receiving) {
//        this(
//                receiving.mpnName,
//                receiving.serialNumber,
//                receiving.conditionName,
//                receiving.status,
//                receiving.locationName,
//                receiving.updatedAt,
//                receiving.post(),
//                receiving.rbid
//        );
//    }
//}
