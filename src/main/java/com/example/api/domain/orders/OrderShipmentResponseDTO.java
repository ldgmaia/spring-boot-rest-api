package com.example.api.domain.orders;

import java.util.List;

public record OrderShipmentResponseDTO(
        List<Shipment> shipments
) {

    public record Shipment(
            String trackingNumber,
            Boolean voided
    ) {
    }
}
