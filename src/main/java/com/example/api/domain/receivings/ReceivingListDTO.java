package com.example.api.domain.receivings;

import com.example.api.domain.receivingitems.ReceivingItemInfoDTO;
import com.example.api.domain.receivingpictures.ReceivingPicturesInfoDTO;
import com.example.api.repositories.InventoryItemRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record ReceivingListDTO(
        Long id,
        String trackingLading,
        String supplierName,
        String carrier,
        ReceivingType type,
        String poNumber,
        String poStatus,
        String parcels,
        String notes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String createdBy,
        List<ReceivingItemInfoDTO> receivingItems,
        List<ReceivingPicturesInfoDTO> pictures
) {
    public ReceivingListDTO(Receiving receiving, InventoryItemRepository inventoryItemRepository) {
        this(
                receiving.getId(),
                receiving.getTrackingLading(),
                receiving.getSupplier().getName(),
                receiving.getCarrier() != null ? receiving.getCarrier().getName() : null,
                receiving.getType(),
                receiving.getPurchaseOrder().getPoNumber(),
                receiving.getPurchaseOrder().getStatus(),
                receiving.getParcels(),
                receiving.getNotes(),
                receiving.getCreatedAt(),
                receiving.getUpdatedAt(),
                receiving.getCreatedBy().getFullName(),
                receiving.getReceivingItems().stream()
                        .map(item -> new ReceivingItemInfoDTO(item, inventoryItemRepository)) // Pass the repository
                        .collect(Collectors.toList()),
                receiving.getPictures().stream()
                        .map(ReceivingPicturesInfoDTO::new)
                        .collect(Collectors.toList())
        );
    }
}
