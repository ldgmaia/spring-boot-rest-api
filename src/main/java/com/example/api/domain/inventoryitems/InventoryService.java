package com.example.api.domain.inventoryitems;

import com.example.api.domain.ValidationException;
import com.example.api.domain.inventoryitems.validations.InventoryValidator;
import com.example.api.repositories.InventoryRepository;
import com.example.api.repositories.ReceivingItemRepository;
import com.example.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class InventoryService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ReceivingItemRepository receivingItemRepository;

    @Autowired
    private List<InventoryValidator> validators; // Spring boot will automatically detect that a List is being ejected and will get all classes that implements this interface and will inject the validators automatically

    public void register(InventoryRequestDTO data) {
        // Validate input data using validators
        validators.forEach(v -> v.validate(data));

        // var condition = conditionRepository.findById(data.conditionId());
        // if(condition.getName() == "New" && data.post() != 'NEW') {
        // throw error

        // Fetch the currently logged-in user
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        var currentUser = userRepository.findByUsername(username);

        if (currentUser == null) {
            throw new RuntimeException("User not found");
        }

        // Fetch the receiving item to get quantity limits
        var receivingItem = receivingItemRepository.findById(data.receivingItemId())
                .orElseThrow(() -> new ValidationException("Receiving item not found"));

        // Get the total number of items already in the inventory for the given receiving_item_id
        var totalItemsInInventory = inventoryRepository.countByReceivingItemId(data.receivingItemId());

        // Check if the current quantity to be added will exceed the quantity received
        var quantityRemainingToAdd = receivingItem.getQuantityToReceive() - totalItemsInInventory;

        if (data.byQuantity() && data.quantity() > quantityRemainingToAdd) {
            throw new ValidationException(
                    String.format("Cannot add %d item(s). Only %d item(s) can be added. ---- 1", data.quantity(), quantityRemainingToAdd));
        }


        if (data.byQuantity()) {
            if (data.quantity() <= 0 || data.quantity() > quantityRemainingToAdd) {
                throw new ValidationException(
                        String.format("Quantity must be between 1 and %d. Current value: %d",
                                quantityRemainingToAdd, data.quantity()));
            }

            if (data.quantity() > 0) {
                for (int i = 0; i < data.quantity(); i++) {
                    var uniqueIdentifier = generateRandomLongInRange(1, Long.MAX_VALUE);
                    var inventory = new Inventory(new InventoryRegisterDTO(
                            data.inventoryId(),
                            data.categoryId(),
                            data.modelId(),
                            data.mpnId(),
                            data.itemConditionsId(),
                            data.locationId(),
                            data.receivingItemId(),
                            currentUser,
                            data.post(),
                            String.valueOf(uniqueIdentifier), // Serial number
                            String.valueOf(uniqueIdentifier) // The RBID will be generated following a formula. This random nunmber is just temporary
                    ), currentUser);

                    inventoryRepository.save(inventory);

                    // After saving, set the `rbid` to be the same as the `id`
//                    savedInventory.setRbid(String.valueOf(savedInventory.getId()));
//                    inventoryRepository.save(savedInventory); // Save again to update the `rbid`

                    // SHOULD UPDATE the quantity ADDED instead, not RECEIVED
//                    receivingItem.setQuantityReceived(data.quantity());
//                    receivingItemRepository.save((receivingItem));
                }
            } else {
                throw new ValidationException("Quantity must be positive. Current value: " + data.quantity());
            }

//            if (data.quantity() > 0) for (int i = 0; i < data.quantity(); i++) inventoryRepository.save(inventory);
//            else throw new ValidationException("quantity value should be positive current value : " + data.quantity());

//            return new InventoryInfoDTO(inventory);
        } else {
            // Add individually by serial number
            // If byQuantity is false, quantity can be null, and we default to 1 for individual items
            long quantityToAdd = (data.quantity() == null) ? 1L : data.quantity();

            if (quantityToAdd > quantityRemainingToAdd) {
                throw new ValidationException(
                        String.format("Cannot add %d item(s). Only %d item(s) can be added. ---- 2",
                                quantityToAdd, quantityRemainingToAdd));
            }

            var uniqueIdentifier = generateRandomLongInRange(1, Long.MAX_VALUE);
            var inventory = new Inventory(new InventoryRegisterDTO(
                    data.inventoryId(),
                    data.categoryId(),
                    data.modelId(),
                    data.mpnId(),
                    data.itemConditionsId(),
                    data.locationId(),
                    data.receivingItemId(),
                    currentUser,
                    data.post(),
                    String.valueOf(uniqueIdentifier), // Serial number
                    String.valueOf(uniqueIdentifier) // The RBID will be generated following a formula. This random nunmber is just temporary
            ), currentUser);

            // Save the inventory to generate the ID
            inventoryRepository.save(inventory);
//            savedInventory.setRbid(String.valueOf(savedInventory.getId()));
//            inventoryRepository.save(savedInventory); // Save again to update the `rbid`
            //inventoryRepository.save(inventory);

//            return new InventoryInfoDTO(inventory);
        }
    }

    public Long generateRandomLongInRange(long min, long max) {
        var random = new Random();
        return min + (long) (random.nextDouble() * (max - min)); // Generates a random Long between min and max
    }

//    public List<CategoryInfoDTO> getAllCategories() {
//        List<Category> categories = categoryRepository.findAll();
//        return categories.stream()
//                .map(this::convertToDto)
//                .collect(Collectors.toList());
//    }
//
//    private CategoryInfoDTO convertToDto(Category category) {
//        return new CategoryInfoDTO(category);
//    }
}
