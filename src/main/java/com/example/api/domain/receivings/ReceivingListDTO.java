package com.example.api.domain.receivings;

public record ReceivingListDTO(
        Long id,
        String trackingLading,
        String supplierName,
        String carrier,
        ReceivingType type,
        Long identifierId,
        String parcels,
        String notes
) {
    public ReceivingListDTO(Receiving receiving) {
        this(
                receiving.getId(),
                receiving.getTrackingLading(),
                receiving.getSupplier().getName(),
                receiving.getCarrier() != null ? receiving.getCarrier().getName() : null,
                receiving.getType(),
                receiving.getIdentifierId(),
                receiving.getParcels(),
                receiving.getNotes()
        );
    }
}


