package com.example.api.domain.assessments;

import com.example.api.domain.ValidationException;
import com.example.api.domain.assessmentfieldsvalues.AssessmentFieldsValues;
import com.example.api.domain.assessmentfieldsvalues.AssessmentFieldsValuesRegisterDTO;
import com.example.api.domain.fieldsvalues.FieldValue;
import com.example.api.domain.fieldsvalues.FieldValueRegisterDTO;
import com.example.api.domain.inventoryitems.InventoryItem;
import com.example.api.domain.inventoryitems.InventoryItemRegisterDTO;
import com.example.api.domain.inventoryitems.InventoryItemService;
import com.example.api.domain.inventoryitemscomponents.InventoryItemComponentRegisterDTO;
import com.example.api.domain.inventoryitemscomponents.InventoryItemComponents;
import com.example.api.domain.inventoryitemsfieldsvalues.InventoryItemsFieldsValues;
import com.example.api.domain.inventoryitemsfieldsvalues.InventoryItemsFieldsValuesRegisterDTO;
import com.example.api.domain.values.Value;
import com.example.api.domain.values.ValueRegisterDTO;
import com.example.api.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AssessmentService {

    @Autowired
    private AssessmentRepository assessmentRepository;

    @Autowired
    private InventoryItemRepository inventoryItemRepository;

    @Autowired
    private InventoryItemComponentRepository inventoryItemComponentRepository;

    @Autowired
    private SectionAreaRepository sectionAreaRepository;

    @Autowired
    private ItemStatusRepository itemStatusRepository;

    @Autowired
    private AssessmentFieldValuesRepository assessmentFieldValuesRepository;

    @Autowired
    private ModelRepository modelRepository;

    @Autowired
    private InventoryItemsFieldValuesRepository inventoryItemsFieldValuesRepository;

    @Autowired
    private MPNRepository mpnRepository;

    @Autowired
    private ValueRepository valueRepository;

    @Autowired
    private FieldRepository fieldRepository;

    @Autowired
    private FieldValueRepository fieldValueRepository;

    @Autowired
    private ItemConditionRepository itemConditionRepository;

    @Autowired
    private ReceivingItemRepository receivingItemRepository;

    @Autowired
    private InventoryItemService inventoryItemService;

    public void createAssessment(AssessmentRequestDTO data) {

        var parentInventoryItem = inventoryItemRepository.getReferenceById(data.parentInventoryItemId());

        if (parentInventoryItem.getItemStatus().getId() != 1L) {
            throw new ValidationException("Item is already in inventory");
        }

        var all = groupFieldsByAreaId(data);

        Assessment parentAssessment = new Assessment(new AssessmentRegisterDTO(
                "Item",
                "Item",
                null,
                parentInventoryItem.getModel().getName(),
                parentInventoryItem.getMpn() != null ? parentInventoryItem.getMpn().getName() : null,
                null,
                "complete",
                parentInventoryItem.getPost(),
                null,
                null,
                null,
                parentInventoryItem.getItemCondition(),
                null,
                parentInventoryItem,
                parentInventoryItem.getReceivingItem()
        ));
        assessmentRepository.save(parentAssessment);

        processMainItemAssessment(data.mainItemSpecs(), parentInventoryItem, parentAssessment);
        processMainItemAssessment(data.mainItemFunctional(), parentInventoryItem, parentAssessment);
        processMainItemAssessment(data.mainItemCosmetic(), parentInventoryItem, parentAssessment);

        all.components().forEach(component -> {
            var area = sectionAreaRepository.findById(component.areaId()).orElse(null);

            InventoryItem inventoryItem;
            if (component.present()) {
                var model = modelRepository.getReferenceById(component.modelId());
                inventoryItem = new InventoryItem(new InventoryItemRegisterDTO(
                        modelRepository.getReferenceById(component.modelId()).getCategory(),
                        model,
                        component.mpnId() != null ? mpnRepository.getReferenceById(component.mpnId()) : null,
                        parentInventoryItem.getItemCondition(),
                        component.pulled() ? itemStatusRepository.getReferenceById(1L) : itemStatusRepository.getReferenceById(3L),
                        parentInventoryItem.getReceivingItem(),
                        parentInventoryItem.getLocation(),
                        "NA",
                        !component.pulled(), // if component was pulled, it should not be present in the inventory items
                        area,
                        component.serialNumber(),
                        "temp",
                        "Component",
                        BigDecimal.ZERO
                ));
                inventoryItemRepository.save(inventoryItem);
                inventoryItem.setRbid(inventoryItemService.generateRBID(inventoryItem));
                inventoryItemRepository.save(inventoryItem);
                inventoryItemComponentRepository.save(new InventoryItemComponents(new InventoryItemComponentRegisterDTO(component.pulled() ? null : parentInventoryItem, inventoryItem)));
            } else {
                inventoryItem = null;
            }

            Assessment assessment;
            if (component.present()) {
                assessment = new Assessment(new AssessmentRegisterDTO(
                        sectionAreaRepository.getReferenceById(component.areaId()).getSection().getName(),
                        sectionAreaRepository.getReferenceById(component.areaId()).getName(),
                        true,
                        modelRepository.getReferenceById(component.modelId()).getName(),
                        component.mpnId() != null ? mpnRepository.getReferenceById(component.mpnId()).getName() : null,
                        component.pulled(),
                        "complete",
                        inventoryItem.getPost() != null ? inventoryItem.getPost() : null,
                        null,
                        null,
                        null,
                        itemConditionRepository.getReferenceById(1L),
                        parentInventoryItem,
                        inventoryItem,
                        inventoryItem.getReceivingItem()
                ));
                assessmentRepository.save(assessment);
            } else {
                assessment = new Assessment(new AssessmentRegisterDTO(
                        sectionAreaRepository.getReferenceById(component.areaId()).getSection().getName(),
                        sectionAreaRepository.getReferenceById(component.areaId()).getName(),
                        false,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        parentInventoryItem,
                        null,
                        null
                ));
                assessmentRepository.save(assessment);
            }

            processComponentAssessmentFields(component.fields(), inventoryItem, assessment);
        });

        // sets the receiving status accordingly
        var received = parentInventoryItem.getReceivingItem().getQuantityAlreadyReceived();
        var assessed = inventoryItemRepository.countByReceivingItemIdAndTypeAndItemStatusIdNotIn(parentInventoryItem.getReceivingItem().getId(), "Main", List.of(1L));

        if (received > 0 && received.equals(assessed)) {
            parentInventoryItem.getReceivingItem().setStatus("Assessment Complete");
        } else {
            parentInventoryItem.getReceivingItem().setStatus("Pending Assessment");
        }

        // 2 means 'In Stock'
        // 3 means 'In Use'
        parentInventoryItem.setItemStatus(itemStatusRepository.getReferenceById(2L));

        // set the Receiving status accordingly
        var totalAssessed = inventoryItemRepository.countByReceivingItemIdAndTypeAndItemStatusIdNot(parentInventoryItem.getReceivingItem().getId(), "Main", 1L);
        var totalAlreadyReceived = receivingItemRepository.getReferenceById(parentInventoryItem.getReceivingItem().getId()).getQuantityAlreadyReceived();
        var totalAdded = inventoryItemRepository.countByReceivingItemIdAndType(parentInventoryItem.getReceivingItem().getId(), "Main");

        if (totalAlreadyReceived.equals(totalAssessed)) {
            receivingItemRepository.getReferenceById(parentInventoryItem.getReceivingItem().getId()).setStatus("Assessment Complete");
        }

        if (totalAdded > totalAssessed) {
            receivingItemRepository.getReferenceById(parentInventoryItem.getReceivingItem().getId()).setStatus("Pending Assessment");
        }
    }

    private void processComponentAssessmentFields(List<AssessmentRequestFieldsDTO> fields, InventoryItem inventoryItem, Assessment assessment) {

        fields.forEach(field -> {

            if (field.fieldId() == null) {
                throw new ValidationException("Field ID is required for " + field);
            }

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

            var assessmentFieldsValues = new AssessmentFieldsValues(new AssessmentFieldsValuesRegisterDTO(fieldValue, assessment));
            assessmentFieldValuesRepository.save(assessmentFieldsValues);
        });
    }

    private void processMainItemAssessment(List<AssessmentRequestFieldsDTO> fields, InventoryItem parentInventoryItem, Assessment parentAssessment) {

        if (fields != null) {
            fields.forEach(field -> {
                if (field.fieldId() == null) {
                    throw new ValidationException("Field ID is required for " + field);
                }

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

                var assessmentFieldsValues = new AssessmentFieldsValues(new AssessmentFieldsValuesRegisterDTO(fieldValue, parentAssessment));
                assessmentFieldValuesRepository.save(assessmentFieldsValues);

                var inventoryItemsFieldsValues = new InventoryItemsFieldsValues(new InventoryItemsFieldsValuesRegisterDTO(fieldValue, parentInventoryItem));
                inventoryItemsFieldValuesRepository.save(inventoryItemsFieldsValues);
            });
        }
    }

    public AssessmentAllFieldsDTO groupFieldsByAreaId(AssessmentRequestDTO data) {
        Map<Long, AssessmentRequestInspectionDTO> areaMap = new HashMap<>();

        List<AssessmentRequestInspectionDTO> combinedSpecs = new ArrayList<>();
        combinedSpecs.addAll(data.specs());
        combinedSpecs.addAll(data.functional());
        combinedSpecs.addAll(data.cosmetic());

        for (AssessmentRequestInspectionDTO spec : combinedSpecs) {
            Long areaId = spec.areaId();
            if (!areaMap.containsKey(areaId)) {
                AssessmentRequestInspectionDTO component = new AssessmentRequestInspectionDTO(spec.present(), spec.areaId(), spec.modelId(), spec.mpnId(), spec.pulled(), spec.serialNumber(), spec.fields());
                areaMap.put(areaId, component);
            } else {
                AssessmentRequestInspectionDTO existingComponent = areaMap.get(areaId);
                if (spec.fields() != null && !spec.fields().isEmpty()) {
                    existingComponent.fields().addAll(spec.fields());
                }
            }
        }

        // Convert map values to a list and build the final DTO
        List<AssessmentRequestInspectionDTO> components = new ArrayList<>(areaMap.values());
        return new AssessmentAllFieldsDTO(data.parentInventoryItemId(), components);
    }
}
