package com.example.api.domain.receivings;

import com.example.api.domain.receivingadditionalitems.ReceivingAdditionalItem;
import com.example.api.domain.receivingadditionalitems.ReceivingAdditionalItemRegisterDTO;
import com.example.api.domain.receivingadditionalitems.ReceivingAdditionalItemRequestDTO;
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
    private ReceivingAdditionalItemRepository receivingAdditionalItemRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private PurchaseOrderItemRepository purchaseOrderItemRepository;


    @Autowired
    private UserRepository userRepository;

    public ReceivingInfoDTO register(ReceivingRequestDTO data) {

        System.out.println("data " + data);

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
        System.out.println("receiving ID " + receiving.getId());

        //add items
        if (data.receivingItems() != null) {
            data.receivingItems()
                    .forEach(
                            (ReceivingItemRequestDTO receivingItemRequestDTO) -> {
                                //System.out.println("**purchaseOrderItemId: " + mfv.purchaseOrderItemId() + "\n");
                                var purchaseOrderItemId = receivingItemRequestDTO.purchaseOrderItemId();
                                var description = receivingItemRequestDTO.description();
                                var quantityToReceive = receivingItemRequestDTO.quantityToReceive();
                                var quantityReceived = receivingItemRequestDTO.quantityReceived();
                                var receivingId = receiving.getId();
                                var receivableItem = receivingItemRequestDTO.receivableItem();

                                var receivingItems = new ReceivingItem(new ReceivingItemRegisterDTO(
                                        receiving,
                                        purchaseOrderItemRepository.getReferenceById(purchaseOrderItemId),
                                        description,
                                        quantityToReceive,
                                        quantityReceived,
                                        currentUser,
                                        receivableItem
                                ));
                                System.out.println("receivingItems " + receivingItems.toString());
                                receivingItemRepository.save(receivingItems);
                            });
        }

        // Additional items record

        if (data.additionalItems() != null) {
            data.additionalItems()
                    .forEach(
                            (ReceivingAdditionalItemRequestDTO receivingAdditionalItemRequestDTO) -> {
                                var description = receivingAdditionalItemRequestDTO.description();
                                var quantityReceived = receivingAdditionalItemRequestDTO.quantityReceived();
                                var receivingAdditionalItems = new ReceivingAdditionalItem(new ReceivingAdditionalItemRegisterDTO(
                                        receiving,
                                        description,
                                        quantityReceived,
                                        currentUser
                                ));

                                receivingAdditionalItemRepository.save(receivingAdditionalItems);

                            });
        }


        //Additional Items

        return new ReceivingInfoDTO(receiving);
    }

}
