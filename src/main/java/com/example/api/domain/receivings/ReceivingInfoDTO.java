package com.example.api.domain.receivings;

import com.example.api.domain.suppliers.SupplierInfoDTO;

public record ReceivingInfoDTO(
        Long id,
        String trackingCode,
        ReceivingType type,
        String identifier,
        SupplierInfoDTO supplier,
        Carriers carrier,
        String notes

) {

    public ReceivingInfoDTO(Receiving receiving) {
        this(
                receiving.getId(),
                receiving.getTrackingLading(),
                receiving.getType(),
                receiving.getIdentifier(),
                new SupplierInfoDTO(receiving.getSupplierId()),
                receiving.getCarrier(),
                receiving.getNotes()
        );
    }
}


