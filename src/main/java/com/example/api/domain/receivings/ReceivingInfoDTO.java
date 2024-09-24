package com.example.api.domain.receivings;

import com.example.api.domain.carriers.CarrierInfoDTO;
import com.example.api.domain.suppliers.SupplierInfoDTO;

public record ReceivingInfoDTO(
        Long id,
        String trackingCode,
        ReceivingType type,
        Long identifierId,
        SupplierInfoDTO supplier,
        CarrierInfoDTO carrier,
        String notes

) {

    public ReceivingInfoDTO(Receiving receiving) {
        this(
                receiving.getId(),
                receiving.getTrackingLading(),
                receiving.getType(),
                receiving.getIdentifierId(),
                receiving.getSupplier() != null ? new SupplierInfoDTO(receiving.getSupplier()) : null,
                receiving.getCarrier() != null ? new CarrierInfoDTO(receiving.getCarrier()) : null,
                receiving.getNotes()
        );
    }
}


