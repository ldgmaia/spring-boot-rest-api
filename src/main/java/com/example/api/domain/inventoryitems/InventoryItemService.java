package com.example.api.domain.inventoryitems;

import com.example.api.domain.ValidationException;
import com.example.api.domain.inventoryitems.validations.InventoryValidator;
import com.example.api.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
public class InventoryItemService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InventoryItemRepository inventoryItemRepository;

    @Autowired
    private ReceivingItemRepository receivingItemRepository;

    @Autowired
    private PurchaseOrderItemRepository purchaseOrderItemRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelRepository modelRepository;

    @Autowired
    private MPNRepository mpnRepository;

    @Autowired
    private ItemConditionRepository itemConditionRepository;

    @Autowired
    private ItemStatusRepository itemStatusRepository;

    @Autowired
    private List<InventoryValidator> validators; // Spring boot will automatically detect that a List is being ejected and will get all classes that implements this interface and will inject the validators automatically

    public List<InventoryItemResponseDTO> register(InventoryItemRequestDTO data) {
        // Validate input data using validators
        validators.forEach(v -> v.validate(data));

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
        var unitsAdded = inventoryItemRepository.countByReceivingItemIdAndType(data.receivingItemId(), "Main");

        // Check if the current quantity to be added will exceed the quantity received
        var quantityRemainingToAdd = receivingItem.getAdditionalItem() ? receivingItem.getQuantityAlreadyReceived() - unitsAdded : receivingItem.getQuantityToReceive() - unitsAdded;

        if (data.byQuantity() && data.quantity() > quantityRemainingToAdd) {
            throw new ValidationException(
                    String.format("Cannot add %d item(s). Only %d item(s) can be added. ---- 1", data.quantity(), quantityRemainingToAdd));
        }

        var typeValue = data.type();

        var poiUnitPrice = receivingItem.getAdditionalItem() ? BigDecimal.valueOf(0L) : receivingItem.getPurchaseOrderItem().getId() != null ? purchaseOrderItemRepository.getReferenceById(receivingItem.getPurchaseOrderItem().getId()).getUnitPrice() : BigDecimal.valueOf(0L);//from purchare order item table on column unit cost

        var location = locationRepository.getReferenceById(1L); // change the id for the correct data when we work on Locations

        List<InventoryItemResponseDTO> inventoryItems = new ArrayList<>();

        if (data.byQuantity()) {
            if (data.quantity() <= 0 || data.quantity() > quantityRemainingToAdd) {
                throw new ValidationException(
                        String.format("Quantity must be between 1 and %d. Current value: %d",
                                quantityRemainingToAdd, data.quantity()));
            }

            if ((unitsAdded + data.quantity()) > receivingItem.getQuantityAlreadyReceived()) {
                throw new ValidationException(String.format("Cannot add %d item(s). because exceed %d that is the quantity received.", data.quantity(), receivingItem.getQuantityAlreadyReceived()));
            }

            if (data.quantity() > 0) {
                for (int i = 0; i < data.quantity(); i++) {
                    var uniqueIdentifier = generateRandomLongInRange(1, Long.MAX_VALUE);
                    var inventory = new InventoryItem(new InventoryItemRegisterDTO(
                            categoryRepository.getReferenceById(data.categoryId()),
                            modelRepository.getReferenceById(data.modelId()),
                            data.mpnId() != null ? mpnRepository.getReferenceById(data.mpnId()) : null,
                            itemConditionRepository.getReferenceById(data.itemConditionId()),
                            itemStatusRepository.getReferenceById(1L),
                            receivingItemRepository.getReferenceById(data.receivingItemId()),
                            location, // this needs to be fixed when we have locations done
                            data.post(),
                            null,
                            null,
                            String.valueOf(uniqueIdentifier), // Serial number - must be given an appropriate value later
                            String.valueOf(uniqueIdentifier), // The RBID will be generated following a formula. This random number is just temporary
                            typeValue,
                            receivingItem.getAdditionalItem() ? BigDecimal.valueOf(0L) : poiUnitPrice
                    ));

                    inventoryItemRepository.save(inventory);
                    receivingItemRepository.getReferenceById(receivingItem.getId()).setQuantityAlreadyReceived(receivingItemRepository.getReferenceById(receivingItem.getId()).getQuantityAlreadyReceived() + 1);


                    receivingItemRepository.getReferenceById(receivingItem.getId()).setStatus("Pending Assessment"); // this line works properly

                    inventoryItems.add(new InventoryItemResponseDTO(inventory));
                }
            } else {
                throw new ValidationException("Quantity must be positive. Current value: " + data.quantity());
            }
        } else {
            if ((unitsAdded + 1) > receivingItem.getQuantityAlreadyReceived()) {
                throw new ValidationException(String.format("Cannot add the item because exceed the quantity received of: %d", receivingItem.getQuantityAlreadyReceived()));
            }
            // Add individually by serial number
            // If byQuantity is false, quantity can be null, and we default to 1 for individual items
            long quantityToAdd = (data.quantity() == null) ? 1L : data.quantity();

            if (quantityToAdd > quantityRemainingToAdd) {
                throw new ValidationException(
                        String.format("Cannot add %d item(s). Only %d item(s) can be added. ---- 2",
                                quantityToAdd, quantityRemainingToAdd));
            }

            var uniqueIdentifier = generateRandomLongInRange(1, Long.MAX_VALUE);
            var inventory = new InventoryItem(new InventoryItemRegisterDTO(
                    categoryRepository.getReferenceById(data.categoryId()),
                    modelRepository.getReferenceById(data.modelId()),
                    data.mpnId() != null ? mpnRepository.getReferenceById(data.mpnId()) : null,
                    itemConditionRepository.getReferenceById(data.itemConditionId()),
                    itemStatusRepository.getReferenceById(1L),
                    receivingItemRepository.getReferenceById(data.receivingItemId()),
                    location, // this needs to be fixed when we have locations done
                    data.post(),
                    null,
                    null,
                    data.serialNumber(), // Serial number
                    String.valueOf(uniqueIdentifier), // The RBID will be generated following a formula. This random nunmber is just temporary
                    typeValue,
                    receivingItem.getAdditionalItem() ? BigDecimal.valueOf(0L) : poiUnitPrice
            ));

            // Save the inventory to generate the ID
            inventoryItemRepository.save(inventory);
            receivingItemRepository.getReferenceById(receivingItem.getId()).setStatus("Pending Assessment");

            inventoryItems.add(new InventoryItemResponseDTO(inventory));
        }

        if (!receivingItem.getAdditionalItem()) {
            var totalByPurchaseOrderId = inventoryItemRepository.countByPurchaseOrderId(receivingItem.getPurchaseOrderItem().getPurchaseOrder().getId());
            var totalOrderedByPurchaseOrderId = purchaseOrderItemRepository.findSumQuantityOrderedByPurchaseOrderId(receivingItem.getPurchaseOrderItem().getPurchaseOrder().getId());

            if (Objects.equals(totalByPurchaseOrderId, totalOrderedByPurchaseOrderId)) {
                var poi = purchaseOrderItemRepository.getReferenceById(receivingItem.getPurchaseOrderItem().getId());
                poi.getPurchaseOrder().setStatus("Fully Added");
            }
        }
        return inventoryItems;
    }

    public Long generateRandomLongInRange(long min, long max) {
        var random = new Random();
        return min + (long) (random.nextDouble() * (max - min)); // Generates a random Long between min and max
    }

    public List<InventoryItemsByReceivingItemDTO> getInventoryItemsByReceivingItemId(Long receivingItemId, String type, Long statusId) {
        return inventoryItemRepository.findByReceivingItemId(receivingItemId, type, statusId);
    }

    public List<InventoryItemsByLocationDTO> getInventoryItemsByLocationId(Long locationId, String type, Long statusId) {
        var inventoryList = inventoryItemRepository.findByLocationIdAndTypeAndItemStatusId(locationId, type, statusId);
        return inventoryList.stream().map(InventoryItemsByLocationDTO::new).toList();
    }

    public InventoryItemInfoDTO listById(Long id) {
        return new InventoryItemInfoDTO(inventoryItemRepository.getReferenceById(id));
    }

    public InventoryItemInfoDTO listBySerialNumber(String serialNumber) {
        try {
            return new InventoryItemInfoDTO(inventoryItemRepository.findBySerialNumber(serialNumber));
        } catch (Exception e) {
            throw new ValidationException("Serial number not found");
        }
    }

    public InventoryItemAssessmentInfoDTO getAssessmentComponentsFieldsByInventoryItemId(Long inventoryItemId) {
        return new InventoryItemAssessmentInfoDTO(Objects.requireNonNull(inventoryItemRepository.findById(inventoryItemId).orElse(null)));
    }

    public InventoryItemInspectionInfoDTO getInspectionComponentsFieldsByInventoryItemId(Long inventoryItemId) {
        return new InventoryItemInspectionInfoDTO(inventoryItemRepository.getReferenceById(inventoryItemId), inventoryItemRepository);
    }
}
