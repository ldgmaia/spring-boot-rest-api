package com.example.api.domain.itemtransfer;


import com.example.api.domain.ValidationException;
import com.example.api.domain.inventoryitems.InventoryItem;
import com.example.api.domain.locations.Location;
import com.example.api.domain.users.User;
import com.example.api.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ItemTransferService {
    private final InventoryItemRepository inventoryItemRepository;
    private final UserRepository userRepository;
    private final AdminSettingsRepository adminSettingsRepository;
    private final LocationRepository locationRepository;
    private final ItemTransferLogRepository itemTransferLogRepository;

    public ItemTransferResponseDTO processTransfer(ItemTransferRequestDTO request) {

        // Fetch item
        InventoryItem item = inventoryItemRepository.findById(request.idItem()).orElseThrow(() -> new ValidationException("Item not found"));

        //validate if initiatorId and receiverId are not null
        if (request.initiatorId() == null || request.receiverId() == null) {
            return new ItemTransferResponseDTO("Initiator and receiver cannot be null", "FAILED");
        }
        //validate if initiatorId and receiverId are not the same
        if (request.initiatorId().equals(request.receiverId())) {
            return new ItemTransferResponseDTO("Initiator and receiver cannot be the same", "FAILED");
        }
        //validate if initiatorId exists
        if (userRepository.findById(request.initiatorId()).isEmpty()) {
            return new ItemTransferResponseDTO("Initiator not found", "FAILED");
        }
        //validate if receiverId exists
        if (userRepository.findById(request.receiverId()).isEmpty()) {
            return new ItemTransferResponseDTO("Receiver not found", "FAILED");
        }
        //validate if item exists
        if (inventoryItemRepository.findById(request.idItem()).isEmpty()) {
            return new ItemTransferResponseDTO("Item not found", "FAILED");
        }
        //validate source location level
        if (item.getLocation() == null || item.getLocation().getId() == null || item.getLocation().getId() <= 0) {
            return new ItemTransferResponseDTO("Source location level is not active", "FAILED");
        }

        // Validate destination location level
        if (item.getLocation() == null || item.getLocation().getId() == null || item.getLocation().getId() <= 0) {
            return new ItemTransferResponseDTO("Destination location level is not active", "FAILED");
        }

        // Fetch initiator and receiver
        User initiator = userRepository.findById(request.initiatorId())
                .orElseThrow(() -> new ValidationException("Initiator not found"));
        User receiver = userRepository.findById(request.receiverId())
                .orElseThrow(() -> new ValidationException("Receiver not found"));

        // Perform transfer logic (e.g., update item location)
        performTransfer(item, receiver);

        // Log successful transfer
        return logTransfer(item, initiator, receiver, "Item transfer successful", "SUCCESS");

    }


    /**
     * Perform the actual transfer by updating the item's location.
     */
    private void performTransfer(InventoryItem item, User receiver) {
        // Assuming the receiver is linked to a new location
        Location l1 = new Location();
        l1.setId(receiver.getId());
        l1.setName(receiver.getUsername());
        l1.setCreatedBy(receiver);
        locationRepository.save(l1);
        item.setLocation(l1);
        inventoryItemRepository.save(item);
    }

    /**
     * Log the transfer operation.
     */
    private ItemTransferResponseDTO logTransfer(
            InventoryItem item,
            User sender,
            User receiver,
            String message,
            String status
    ) {
        // Create log entry
        ItemTransferLog log = new ItemTransferLog();
        log.setItem(item);
        log.setInitiator(sender);
        log.setReceiver(receiver);
        log.setCreatedBy(sender);
        log.setTransferDate(LocalDateTime.now());

        // Save log entry to the database
        itemTransferLogRepository.save(log);

        // Return response DTO
        return new ItemTransferResponseDTO(message, status);
    }

}
