package com.example.api.domain.receivingitems;

import com.example.api.domain.ValidationException;
import com.example.api.domain.receivings.ReceivingType;
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
            return receivingItemRepository.findByReceivingTypeAndQuantityAlreadyReceivedGreaterThanAndStatusInOrderByCreatedAtDesc(ReceivingType.PO, 0L, data.status()).stream().map(ri -> {
                return new ReceivingItemAssessmentListDTO(ri, inventoryItemRepository);
            }).toList();
        }

        switch (data.criteria()) {
            case "serialNumber" -> {
                return receivingItemRepository.findByInventoryItemsSerialNumberAndReceivingTypeOrderByCreatedAtDesc(data.value(), ReceivingType.PO).stream().map(ri -> {
                    return new ReceivingItemAssessmentListDTO(ri, inventoryItemRepository);
                }).toList();
//                return receivingItemRepository.findReceivedItemsBySerialNumber(data.value());
            }
            case "receivingId" -> {
                return receivingItemRepository.findByReceivingTypeAndReceivingIdAndStatusInOrderByCreatedAtDesc(ReceivingType.PO, Long.valueOf(data.value()), data.status()).stream().map(ri -> {
                    return new ReceivingItemAssessmentListDTO(ri, inventoryItemRepository);
                }).toList();
//                return receivingItemRepository.findReceivedItemsByReceivingId(data.status(), Long.valueOf(data.value()));
            }
            case "description" -> {
                return receivingItemRepository.findByReceivingTypeAndStatusInAndDescriptionContainingOrderByCreatedAtDesc(ReceivingType.PO, data.status(), data.value()).stream().map(ri -> {
                    return new ReceivingItemAssessmentListDTO(ri, inventoryItemRepository);
                }).toList();
            }
            case "trackingLading" -> {
                return receivingItemRepository.findByReceivingTypeAndStatusInAndReceivingTrackingLadingOrderByCreatedAtDesc(ReceivingType.PO, data.status(), data.value()).stream().map(ri -> {
                    return new ReceivingItemAssessmentListDTO(ri, inventoryItemRepository);
                }).toList();
            }
            case "supplierId" -> {
                return receivingItemRepository.findByReceivingTypeAndStatusInAndReceivingSupplierIdOrderByCreatedAtDesc(ReceivingType.PO, data.status(), Long.valueOf(data.value())).stream().map(ri -> {
                    return new ReceivingItemAssessmentListDTO(ri, inventoryItemRepository);
                }).toList();
            }
            case "poNumber" -> {
                return receivingItemRepository.findByReceivingTypeAndPurchaseOrderItemPurchaseOrderPoNumberAndStatusInOrderByCreatedAtDesc(ReceivingType.PO, data.value(), data.status()).stream().map(ri -> {
                    return new ReceivingItemAssessmentListDTO(ri, inventoryItemRepository);
                }).toList();
            }
            default -> {
                throw new IllegalArgumentException("Invalid criteria: " + data.criteria() + ". Expected criteria.");
            }
        }
    }

}
