package com.example.api.domain.receivings;

import com.example.api.domain.receivingitems.ReceivingItemsInfoDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record ReceivingListDTO(
        Long id,
        String trackingLading,
        String supplierName,
        String carrier,
        ReceivingType type,
        Long identifierId,
        String parcels,
        String notes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String createdBy,
        List<ReceivingItemsInfoDTO> receivingItems
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
                receiving.getNotes(),
                receiving.getCreatedAt(),
                receiving.getUpdatedAt(),
                receiving.getCreatedBy().getFullName(),
                receiving.getReceivingItems().stream()
                        .map(ReceivingItemsInfoDTO::new)
                        .collect(Collectors.toList()) // Convert to DTO list
        );
    }
}


