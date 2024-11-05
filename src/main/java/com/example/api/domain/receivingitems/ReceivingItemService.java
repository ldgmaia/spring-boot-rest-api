package com.example.api.domain.receivingitems;

import com.example.api.domain.ValidationException;
import com.example.api.repositories.ReceivingItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReceivingItemService {

    @Autowired
    private ReceivingItemRepository receivingItemRepository;

    public ReceivingItemInfoDTO show(Long id) {
        var receivingItem = receivingItemRepository.findById(id).orElseThrow(() -> new ValidationException("Receiving not found"));
        return new ReceivingItemInfoDTO(receivingItem);
    }

    public List<ReceivingItemAssessmentListDTO> list(ReceivingItemListByRequestDTO data) {
        switch (data.criteria()) {
            case "serialNumber" -> {
                List<ReceivingItemAssessmentListDTO> receivingItemsBySerialNumber = receivingItemRepository.findReceivedItemsBySerialNumber(data.value());

                return receivingItemsBySerialNumber;
            }
            case "receivingId" -> {
                List<ReceivingItemAssessmentListDTO> receivingItemsReceivingId = receivingItemRepository.findReceivedItemsByReceivingId(data.status(), Long.valueOf(data.value()));
                return receivingItemsReceivingId;
            }
            case "description" -> {
                List<ReceivingItemAssessmentListDTO> receivingItemsByDescription = receivingItemRepository.findReceivedItemsByDescription(data.status(), data.value());
                return receivingItemsByDescription;
            }
            case "trackingLading" -> {
                List<ReceivingItemAssessmentListDTO> receivingsItemsByTrakingLading = receivingItemRepository.findReceivedItemsByTrackinglading(data.status(), data.value());
                return receivingsItemsByTrakingLading;
            }
            case "supplierId" -> {
                List<ReceivingItemAssessmentListDTO> receivingsItemsBySupplierId = receivingItemRepository.findReceivedItemsBySupplierId(data.status(), Long.valueOf(data.value()));
                return receivingsItemsBySupplierId;
            }
            case "poNumber" -> {
                List<ReceivingItemAssessmentListDTO> receivingItemsByPoNumber = receivingItemRepository.findReceivedItemsByPurchaseOrderNumber(data.status(), data.value());
                return receivingItemsByPoNumber;
            }
            default -> {
                throw new IllegalArgumentException("Invalid criteria: " + data.criteria() + ". Expected criteria are 'serialNumber' or 'receivingId'.");
            }
        }
    }

}
