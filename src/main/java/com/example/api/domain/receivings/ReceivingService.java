package com.example.api.domain.receivings;

import com.example.api.domain.ValidationException;
import com.example.api.domain.receivingitems.ReceivingItem;
import com.example.api.domain.receivingitems.ReceivingItemRegisterDTO;
import com.example.api.domain.receivingitems.ReceivingItemRequestDTO;
import com.example.api.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ReceivingService {

    @Autowired
    private ReceivingRepository receivingRepository;

    @Autowired
    private ReceivingItemRepository receivingItemRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private PurchaseOrderItemRepository purchaseOrderItemRepository;

    @Autowired
    private UserRepository userRepository;

    public ReceivingInfoDTO register(ReceivingRequestDTO data) {

        // Fetch the currently logged-in user
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        var currentUser = userRepository.findByUsername(username);
        var receiving = new Receiving(new ReceivingRegisterDTO(
                data.trackingCode(),
                data.type(),
                data.identifier(),
                supplierRepository.getReferenceById(data.supplierId()),
                data.carrier(),
                data.notes()
        ), currentUser);

        receivingRepository.save(receiving);


        // create the receiving record to get its ID
        //add items
        if (data.receivingItems() != null) {
            data.receivingItems()
                    .forEach(
                            (ReceivingItemRequestDTO receivingItem) -> {

                                if (!receivingItem.additionalItem()) {
                                    if (receivingItem.purchaseOrderItemId() == null) {
                                        throw new ValidationException("Missing purchase order item");
                                    }
                                    if (receivingItem.quantityToReceive() == null) {
                                        throw new ValidationException("Quantity to receive cannot be empty");
                                    }
                                }

                                var receivingItems = new ReceivingItem(new ReceivingItemRegisterDTO(
                                        receiving,
//                                        Optional.ofNullable(receivingItem.purchaseOrderItemId())
//                                                .map(purchaseOrderItemRepository::getReferenceById)
//                                                .orElse(null),
                                        receivingItem.purchaseOrderItemId() == null ? null : purchaseOrderItemRepository.getReferenceById(receivingItem.purchaseOrderItemId()),
                                        receivingItem.description(),
                                        receivingItem.quantityToReceive() == null ? null : receivingItem.quantityToReceive(),
                                        receivingItem.quantityReceived(),
                                        currentUser,
                                        receivingItem.receivableItem(),
                                        receivingItem.additionalItem()
                                ));
                                System.out.println("receivingItems - before save" + receivingItems);
                                receivingItemRepository.save(receivingItems);
                            });
        }

        return new ReceivingInfoDTO(receiving);
    }

    public ReceivingInfoDTO show(Long id) {
        var receivingById = receivingRepository.findById(id).orElseThrow(() -> new ValidationException("Receiving not found"));
        return new ReceivingInfoDTO(receivingById);
    }

}
