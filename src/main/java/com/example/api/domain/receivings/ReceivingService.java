package com.example.api.domain.receivings;

import com.example.api.domain.ValidationException;
import com.example.api.domain.carriers.Carrier;
import com.example.api.domain.files.File;
import com.example.api.domain.files.FileService;
import com.example.api.domain.receivingitems.ReceivingItem;
import com.example.api.domain.receivingitems.ReceivingItemRegisterDTO;
import com.example.api.domain.receivingitems.ReceivingItemRequestDTO;
import com.example.api.domain.receivingpictures.ReceivingPicture;
import com.example.api.domain.receivingpictures.ReceivingPictureRegisterDTO;
import com.example.api.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
public class ReceivingService {

    @Autowired
    private ReceivingRepository receivingRepository;

    @Autowired
    private ReceivingItemRepository receivingItemRepository;

    @Autowired
    private ReceivingPictureRepository receivingPictureRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private PurchaseOrderItemRepository purchaseOrderItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private CarrierRepository carrierRepository;

    public ReceivingInfoDTO register(ReceivingRequestDTO data, MultipartFile[] pictures) {

        // Fetch the currently logged-in user
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        var currentUser = userRepository.findByUsername(username);

        // Get the carrier if necessary
        Carrier carrier = null;
        if (data.carrierId() != null) {
            carrier = carrierRepository.findById(data.carrierId()).orElseThrow(() -> new RuntimeException("Carrier not found"));
        }

        // Create the receiving header to generate the ID
        var receiving = new Receiving(new ReceivingRegisterDTO(
                data.trackingCode(),
                data.type(),
                supplierRepository.getReferenceById(data.supplierId()),
                purchaseOrderRepository.getReferenceById(data.purchaseOrderId()),
                carrier,
                data.notes()
        ), currentUser);

        receivingRepository.save(receiving);

        // Save all pictures/files for the receiving. TODO: save files in some cloud service instead of the same server running the backend
        List<File> savedFiles = null;
        try {
            savedFiles = fileService.saveFiles(pictures);
            for (File file : savedFiles) {
                receivingPictureRepository.save(new ReceivingPicture(new ReceivingPictureRegisterDTO(receiving, file, currentUser)));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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

                                //var receivingId = receiving.getId();
                                var quantityToReceive = receivingItem.quantityToReceive();
                                var quantityReceived = receivingItem.quantityReceived();
                                var totalQuantityReceived = receivingItemRepository.findQuantityReceivedByPurchaseOrderItemId(receivingItem.purchaseOrderItemId());// Get total quantity already received for this receiving_id

                                // Check if adding the current item exceeds the allowed quantity
                                if (!receivingItem.additionalItem() && totalQuantityReceived + quantityReceived > quantityToReceive) {
//                                    System.err.println("Can not add " + quantityReceived + " item(s) into " + quantityToReceive + " because there is Already " + totalQuantityReceived + " added");
                                    var poi = purchaseOrderItemRepository.getReferenceById(receivingItem.purchaseOrderItemId());
                                    throw new ValidationException("Total received quantity exceeds the quantity ordered for the item: " + poi.getDescription());
                                }

                                var receivingItems = new ReceivingItem(new ReceivingItemRegisterDTO(
                                        receiving,
//                                        Optional.ofNullable(receivingItem.purchaseOrderItemId())
//                                                .map(purchaseOrderItemRepository::getReferenceById)
//                                                .orElse(null),
                                        receivingItem.purchaseOrderItemId() == null ? null : purchaseOrderItemRepository.getReferenceById(receivingItem.purchaseOrderItemId()),
                                        receivingItem.description(),
                                        receivingItem.quantityToReceive(),
                                        receivingItem.quantityReceived(),
                                        currentUser,
                                        receivingItem.receivableItem(),
                                        receivingItem.additionalItem()
                                ));

//                                System.out.println("receivingItems - before save" + receivingItems);

                                if (!Objects.equals(quantityReceived, quantityToReceive)) {
                                    receivingItems.setStatus("Partially Received");
                                } else {
                                    receivingItems.setStatus("Fully Received");
                                    purchaseOrderRepository.getReferenceById(receivingItem.purchaseOrderItemId()).setStatus("Fully Received");
                                }

                                //before this step validate if there are enough spaces to add the items
                                receivingItemRepository.save(receivingItems);
                                // Update the overall receiving status based on all items received
                                updateReceivingStatus(receiving.getId(), receivingItem.purchaseOrderItemId());

                            });

        } else {
            throw new ValidationException("Quantity received is invalid");
        }

        return new ReceivingInfoDTO(receiving);
    }

    public ReceivingInfoDTO show(Long id) {
        var receiving = receivingRepository.findById(id).orElseThrow(() -> new ValidationException("Receiving not found"));
        return new ReceivingInfoDTO(receiving);
    }

    /**
     * This method checks the total quantity received for each purchase order item and updates the status accordingly.
     */
    private void updateReceivingStatus(Long receivingId, Long purchaseOrderItemId) {
        // Fetch the total received quantity for the given purchase order item
        var totalReceived = receivingItemRepository.findTotalReceivedQuantityByPurchaseOrderItemId(purchaseOrderItemId);

        // Fetch the ordered quantity for the given purchase order item
        var quantityOrdered = purchaseOrderItemRepository.findQuantityOrderedById(purchaseOrderItemId);

        // Compare and update the status
        if (Objects.equals(totalReceived, quantityOrdered)) {
            receivingItemRepository.updateStatusByPurchaseOrderItemId(purchaseOrderItemId, "Fully Received");
            purchaseOrderRepository.getReferenceById(purchaseOrderItemId).setStatus("Fully Received");
        } else {
            receivingItemRepository.updateStatusByPurchaseOrderItemId(purchaseOrderItemId, "Partially Received");
        }
    }

}
