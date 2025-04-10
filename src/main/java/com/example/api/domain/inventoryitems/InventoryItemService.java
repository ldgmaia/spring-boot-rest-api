package com.example.api.domain.inventoryitems;

import com.example.api.domain.ValidationException;
import com.example.api.domain.fieldsvalues.FieldValue;
import com.example.api.domain.fieldsvalues.FieldValueRegisterDTO;
import com.example.api.domain.inventoryitems.inspection.*;
import com.example.api.domain.inventoryitems.validations.InventoryValidator;
import com.example.api.domain.inventoryitemsfieldsvalues.InventoryItemsFieldsValues;
import com.example.api.domain.inventoryitemsfieldsvalues.InventoryItemsFieldsValuesRegisterDTO;
import com.example.api.domain.values.Value;
import com.example.api.domain.values.ValueRegisterDTO;
import com.example.api.repositories.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InventoryItemService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InventoryItemRepository inventoryItemRepository;

    @Autowired
    private InventoryItemsFieldValuesRepository inventoryItemsFieldValuesRepository;

    @Autowired
    private InventoryItemComponentRepository inventoryItemComponentRepository;

    @Autowired
    private ReceivingItemRepository receivingItemRepository;

    @Autowired
    private PurchaseOrderItemRepository purchaseOrderItemRepository;

    @Autowired
    private StorageLevelRepository storageLevelRepository;

    @Autowired
    private FieldRepository fieldRepository;

    @Autowired
    private ValueRepository valueRepository;

    @Autowired
    private FieldValueRepository fieldValueRepository;

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
    private SectionAreaRepository sectionAreaRepository;

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

        var location = storageLevelRepository.getReferenceById(currentUser.getStorageLevel().getId()); // change the id for the correct data when we work on Locations

        List<InventoryItemResponseDTO> inventoryItems = new ArrayList<>();

        String companygrade = null;
        Long conditionId = data.itemConditionId();

        if (data.itemConditionId() == 1L) { // NEW
            companygrade = "N";
        } else {
            if (Objects.equals(data.post(), "FAILED")) {
                companygrade = "FA";
                conditionId = 5L; // DOA
            } else {
                companygrade = "TBI";
            }
        }

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
                            itemConditionRepository.getReferenceById(conditionId),
                            itemStatusRepository.getReferenceById(1L),
                            receivingItemRepository.getReferenceById(data.receivingItemId()),
                            location, // this needs to be fixed when we have locations done
                            data.post(),
                            null,
                            null,
                            String.valueOf(uniqueIdentifier), // Serial number - must be given an appropriate value later
                            String.valueOf(uniqueIdentifier), // This is a temporal value to be replaces with the method generateRBID
                            typeValue,
                            receivingItem.getAdditionalItem() ? BigDecimal.valueOf(0L) : poiUnitPrice,
                            companygrade
                    ));

                    inventoryItemRepository.save(inventory);
                    inventory.setRbid(generateRBID(inventory));
                    inventoryItemRepository.save(inventory);

                    receivingItemRepository.getReferenceById(receivingItem.getId()).setQuantityAlreadyReceived(receivingItemRepository.getReferenceById(receivingItem.getId()).getQuantityAlreadyReceived() + 1);
                    receivingItemRepository.getReferenceById(receivingItem.getId()).setStatus("Pending Assessment");

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
                    itemConditionRepository.getReferenceById(conditionId),
                    itemStatusRepository.getReferenceById(1L),
                    receivingItemRepository.getReferenceById(data.receivingItemId()),
                    location, // this needs to be fixed when we have locations done
                    data.post(),
                    null,
                    null,
                    data.serialNumber(), // Serial number
                    String.valueOf(uniqueIdentifier), // The RBID will be generated following a formula. This random nunmber is just temporary
                    typeValue,
                    receivingItem.getAdditionalItem() ? BigDecimal.valueOf(0L) : poiUnitPrice,
                    companygrade
            ));

            // Save the inventory to generate the ID
            inventoryItemRepository.save(inventory);
            String rbid = generateRBID(inventory);//to generate with partNumber
            inventory.setRbid(rbid);
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

    public List<InventoryItemsByReceivingItemDTO> getInventoryItemsByReceivingItemId(Long receivingItemId, String type, Long itemStatusId) {
        return inventoryItemRepository.findByReceivingItemId(receivingItemId, itemStatusId, type);
    }

    public List<InventoryItemsByLocationDTO> getInventoryItemsByLocationId(Long locationId, String type, Long statusId, Long mainItemInventoryId, Long areaId) {
        var inventoryList = inventoryItemRepository.findByStorageLevelIdAndTypeAndItemStatusId(locationId, type, statusId, mainItemInventoryId, areaId);
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

    public InventoryItemInspectedItemInfoDTO getInspectedItemInfoByInventoryItemId(Long inventoryItemId) {
        return new InventoryItemInspectedItemInfoDTO(inventoryItemRepository.getReferenceById(inventoryItemId));
    }

    public List<InventoryItemLookUpResponseDTO> lookup(InventoryItemLookUpRequestDTO requestData) {

        var filteredInventoryItems = inventoryItemRepository
                .findByModelIdAndItemStatusIdIn(requestData.modelId(), requestData.statusesIds())
                .stream()
                .filter(ii -> ii.getInventoryItemsFieldsValues().stream()
                        .anyMatch(iifv -> requestData.fieldsTypes().contains(
                                iifv.getFieldValue().getField().getFieldType().name()
                        ))
                )
                .toList();

        return filteredInventoryItems.stream().map(InventoryItemLookUpResponseDTO::new).toList();
    }

    public List<InventoryItemLookUpComponentsResponseDTO> lookupComponents(InventoryItemLookUpRequestDTO requestData) {

        List<InventoryItemLookUpComponentsResponseDTO> result = inventoryItemRepository
                .findByModelIdAndItemStatusIdIn(requestData.modelId(), requestData.statusesIds())
                .stream()
                .map(ii -> {
                    Map<Long, InventoryItemLookUpComponentsResponseDTO.AreaDto> areaMap = new LinkedHashMap<>();

                    ii.getInventoryItemComponents().forEach(iic -> {
                        var component = iic.getInventoryItem();
                        var model = component.getModel();

                        component.getInventoryItemsFieldsValues().forEach(iifv -> {
                            var field = iifv.getFieldValue().getField();

                            if (requestData.fieldsTypes().contains(field.getFieldType().name())) {
                                String valueData = iifv.getFieldValue().getValueData().getValueData();
                                Long fieldId = field.getId();

                                model.getSectionAreaModels().forEach(sam -> {
                                    var area = sam.getSectionArea();
                                    var areaId = area.getId();

                                    // Get or create the AreaDto
                                    var areaDto = areaMap.computeIfAbsent(areaId, k ->
                                            new InventoryItemLookUpComponentsResponseDTO.AreaDto(areaId, area.getName(), new ArrayList<>())
                                    );

                                    // Deduplication: check if field already exists in that area's fields list
                                    boolean alreadyExists = areaDto.fields().stream().anyMatch(f ->
                                            f.fieldId().equals(fieldId) && f.valueData().equals(valueData)
                                    );

                                    if (!alreadyExists) {
                                        areaDto.fields().add(
                                                new InventoryItemLookUpComponentsResponseDTO.FieldDto(
                                                        field.getId(),
                                                        field.getName(),
                                                        iifv.getFieldValue().getValueData().getId(),
                                                        valueData
                                                )
                                        );
                                    }
                                });
                            }
                        });
                    });

                    return new InventoryItemLookUpComponentsResponseDTO(ii.getId(), new ArrayList<>(areaMap.values()));
                })
                .toList();
        return result;
    }

    public void saveInspection(@Valid InventoryItemSaveInspectionRequestDTO data) {

        // Combine all main fields into one
        List<InventoryItemSaveInspectionFieldsRequestDTO> mainItemFieldsCombined = new ArrayList<>();

        if (data.mainItemSpecs() != null) {
            mainItemFieldsCombined.addAll(data.mainItemSpecs());
        }

        if (data.mainItemFunctional() != null) {
            mainItemFieldsCombined.addAll(data.mainItemFunctional());
        }

        if (data.mainItemCosmetic() != null) {
            mainItemFieldsCombined.addAll(data.mainItemCosmetic());
        }

        // Combine all components into one
        List<InventoryItemSaveInspectionAreaRequestDTO> componentsCombined = new ArrayList<>();
        if (data.specs() != null) {
            componentsCombined.addAll(data.specs());
        }
        if (data.functional() != null) {
            componentsCombined.addAll(data.functional());
        }
        if (data.cosmetic() != null) {
            componentsCombined.addAll(data.cosmetic());
        }

        // Group by areaId
        Map<Long, InventoryItemSaveInspectionAreaRequestDTO> groupedByArea = componentsCombined.stream()
                .collect(Collectors.toMap(
                        InventoryItemSaveInspectionAreaRequestDTO::areaId,
                        dto -> dto,
                        (existing, incoming) -> {
                            // Merge fields and other properties
                            List<InventoryItemSaveInspectionFieldsRequestDTO> mergedFields = new ArrayList<>();
                            if (existing.fields() != null) mergedFields.addAll(existing.fields());
                            if (incoming.fields() != null) mergedFields.addAll(incoming.fields());

                            return new InventoryItemSaveInspectionAreaRequestDTO(
                                    existing.present() != null ? existing.present() : incoming.present(),
                                    existing.section() != null ? existing.section() : incoming.section(),
                                    existing.area() != null ? existing.area() : incoming.area(),
                                    existing.areaId(),
                                    existing.modelId() != null ? existing.modelId() : incoming.modelId(),
                                    existing.mpnId() != null ? existing.mpnId() : incoming.mpnId(),
                                    existing.inventoryItemId() != null ? existing.inventoryItemId() : incoming.inventoryItemId(),
                                    existing.needsMpn() != null ? existing.needsMpn() : incoming.needsMpn(),
                                    existing.needsSerialNumber() != null ? existing.needsSerialNumber() : incoming.needsSerialNumber(),
                                    mergedFields
                            );
                        }
                ));

        // Removing all fields values from the main item and its components
        inventoryItemsFieldValuesRepository.deleteByInventoryItemId(data.parentInventoryItemId());
        inventoryItemComponentRepository.updateInventoryItemsPresenceToFalse(data.parentInventoryItemId());
        inventoryItemComponentRepository.removeParentItemOfComponents(data.parentInventoryItemId());

        List<Long> componentInventoryItemIds = new ArrayList<>();
        for (InventoryItemSaveInspectionAreaRequestDTO area : groupedByArea.values()) {
            componentInventoryItemIds.add(area.inventoryItemId());
        }
        inventoryItemsFieldValuesRepository.deleteByInventoryItemIdIn(componentInventoryItemIds);

        // add the parent for the new components
        for (var component : groupedByArea.values()) {
            if (component.present()) {
                var ii = inventoryItemRepository.getReferenceById(component.inventoryItemId());
                ii.setModel(modelRepository.getReferenceById(component.modelId()));
                ii.setSectionArea(sectionAreaRepository.getReferenceById(component.areaId()));
                ii.setItemStatus(itemStatusRepository.getReferenceById(3L)); // in use
                ii.setPresent(true);
                if (component.mpnId() != null) {
                    ii.setMpn(mpnRepository.getReferenceById(component.mpnId()));
                } else {
                    ii.setMpn(null);
                }
                inventoryItemRepository.save(ii);

                if (component.fields() != null) {
                    for (var field : component.fields()) {
                        var specField = fieldRepository.getReferenceById(field.fieldId());

                        Value valueData;

                        // Case 1: Both valueData and valueDataId are null
                        if (field.valueDataId() == null && (field.valueData() == null || field.valueData().isEmpty())) {
                            throw new ValidationException("Value is required for " + field);
                        }

                        // Case 2: valueDataId exists
                        if (field.valueDataId() != null) {
                            valueData = valueRepository.getReferenceById(field.valueDataId());
                        } else {
                            // Case 3: valueData provided, valueDataId must be null
                            if (valueRepository.existsByValueData(field.valueData())) {
                                valueData = valueRepository.findByValueData(field.valueData());
                            } else {
                                valueData = valueRepository.save(new Value(new ValueRegisterDTO(field.valueData())));
                            }
                        }

                        // check if field value already exists
                        FieldValue fieldValue;
                        if (fieldValueRepository.existsByValuesDataIdAndFieldsId(valueData.getId(), specField.getId())) {
                            fieldValue = fieldValueRepository.findByFieldIdAndValueDataId(specField.getId(), valueData.getId());
                        } else {
                            Double score = null;
                            if (specField.getFieldType().name().equals("COSMETIC")) {
                                score = 9.0;
                            } else if (specField.getFieldType().name().equals("FUNCTIONAL")) {
                                score = 5.0;
                            }
                            var newFieldvalue = new FieldValue(new FieldValueRegisterDTO(valueData, score, specField));
                            fieldValue = fieldValueRepository.save(newFieldvalue);
                        }

                        var inventoryItemsFieldsValues = new InventoryItemsFieldsValues(new InventoryItemsFieldsValuesRegisterDTO(fieldValue, ii));
                        inventoryItemsFieldValuesRepository.save(inventoryItemsFieldsValues);
                    }
                }
            }
        }
        inventoryItemComponentRepository.addParentItemToComponents(data.parentInventoryItemId(), componentInventoryItemIds);

        var inventoryItem = inventoryItemRepository.getReferenceById(data.parentInventoryItemId());

        for (var field : mainItemFieldsCombined) {
            var specField = fieldRepository.getReferenceById(field.fieldId());

            Value valueData;

            // Case 1: Both valueData and valueDataId are null
            if (field.valueDataId() == null && (field.valueData() == null || field.valueData().isEmpty())) {
                throw new ValidationException("Value is required for " + field);
            }

            // Case 2: valueDataId exists
            if (field.valueDataId() != null) {
                valueData = valueRepository.getReferenceById(field.valueDataId());
            } else {
                // Case 3: valueData provided, valueDataId must be null
                if (valueRepository.existsByValueData(field.valueData())) {
                    valueData = valueRepository.findByValueData(field.valueData());
                } else {
                    valueData = valueRepository.save(new Value(new ValueRegisterDTO(field.valueData())));
                }
            }

            // check if field value already exists
            FieldValue fieldValue;
            if (fieldValueRepository.existsByValuesDataIdAndFieldsId(valueData.getId(), specField.getId())) {
                fieldValue = fieldValueRepository.findByFieldIdAndValueDataId(specField.getId(), valueData.getId());
            } else {
                Double score = null;
                if (specField.getFieldType().name().equals("COSMETIC")) {
                    score = 9.0;
                } else if (specField.getFieldType().name().equals("FUNCTIONAL")) {
                    score = 5.0;
                }
                var newFieldvalue = new FieldValue(new FieldValueRegisterDTO(valueData, score, specField));
                fieldValue = fieldValueRepository.save(newFieldvalue);
            }

            var inventoryItemsFieldsValues = new InventoryItemsFieldsValues(new InventoryItemsFieldsValuesRegisterDTO(fieldValue, inventoryItem));
            inventoryItemsFieldValuesRepository.save(inventoryItemsFieldsValues);
        }
    }

    public String generateRBID(InventoryItem inventoryItem) {
        // Fetch required data
        String partNumber = inventoryItem.getMpn() != null ? inventoryItem.getMpn().getName() : "";
        String model = inventoryItem.getModel().getName(); // Assuming Model has a 'name' field
        String category = inventoryItem.getCategory().getName(); // Assuming Category has a 'name' field
        String po = inventoryItem.getReceivingItem().getPurchaseOrderItem().getPurchaseOrder().getPoNumber(); // Assuming PurchaseOrder has a 'poNumber' field
        Long inventoryItemsId = inventoryItem.getId();

        // Build the RBID
        return String.format("%s+%s+%s+%s+%d", partNumber, model, category, po, inventoryItemsId);
    }
}
