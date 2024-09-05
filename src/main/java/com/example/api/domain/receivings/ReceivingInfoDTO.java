package com.example.api.domain.receivings;

import com.example.api.domain.suppliers.SupplierInfoDTO;

public record ReceivingInfoDTO(
        String trackingCode,
        ReceivingType type,
        String identifier,
        SupplierInfoDTO supplier,
        String poNumber,
        Carriers carrier,
        String notes
//        List<Long> pictureIds,
//        List<ReceivingItemsDTO> receivingItems,
//        List<AdditionalItemsDTO> additionalItems

) {

    public ReceivingInfoDTO(Receiving receiving) {
        this(
                receiving.getTrackingLading(),
                receiving.getType(),
                receiving.getIdentifier(),
                new SupplierInfoDTO(receiving.getSupplierId()),
                receiving.getPurchaseOrder().getPoNumber(),
                receiving.getCarrier(),
                receiving.getNotes()
        );
    }
}


