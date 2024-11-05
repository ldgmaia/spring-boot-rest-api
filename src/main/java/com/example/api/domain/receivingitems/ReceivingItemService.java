package com.example.api.domain.receivingitems;

import com.example.api.domain.ValidationException;
import com.example.api.repositories.InventoryItemRepository;
import com.example.api.repositories.ReceivingItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ReceivingItemService {

    @Autowired
    private ReceivingItemRepository receivingItemRepository;

    @Autowired
    private InventoryItemRepository inventoryItemRepository;

    public ReceivingItemInfoDTO show(Long id) {
        var receivingItem = receivingItemRepository.findById(id).orElseThrow(() -> new ValidationException("Receiving not found"));
        return new ReceivingItemInfoDTO(receivingItem, inventoryItemRepository);
    }

    public List<ReceivingItemAssessmentListDTO> list(ReceivingItemListByRequestDTO data) {

        if (Objects.equals(data.value(), "") || data.value() == null) {
            return receivingItemRepository.listReceivingsByStatus(data.status());
        }

        switch (data.criteria()) {
            case "serialNumber" -> {
                return receivingItemRepository.findReceivedItemsBySerialNumber(data.value());
            }
            case "receivingId" -> {
                return receivingItemRepository.findReceivedItemsByReceivingId(data.status(), Long.valueOf(data.value()));
            }
            case "description" -> {
                return receivingItemRepository.findReceivedItemsByDescription(data.status(), data.value());
            }
            case "trackingLading" -> {
                return receivingItemRepository.findReceivedItemsByTrackinglading(data.status(), data.value());
            }
            case "supplierId" -> {
                return receivingItemRepository.findReceivedItemsBySupplierId(data.status(), Long.valueOf(data.value()));
            }
            case "poNumber" -> {
                return receivingItemRepository.findReceivedItemsByPurchaseOrderNumber(data.status(), data.value());
            }
            default -> {
                throw new IllegalArgumentException("Invalid criteria: " + data.criteria() + ". Expected criteria.");
            }
        }
    }

}
