package com.example.api.domain.receivings;

public record ReceivingListDTO(
        Long id,
        String trackingLading,
        Carriers carrier,
        ReceivingType type,
        String identifier,
        String parcels,
        String notes
) {
    public ReceivingListDTO(Receiving receiving) {
        this(
                receiving.getId(),
                receiving.getTrackingLading(),
                receiving.getCarrier(),
                receiving.getType(),
                receiving.getIdentifier(),
                receiving.getParcels(),
                receiving.getNotes()
        );
    }
}


