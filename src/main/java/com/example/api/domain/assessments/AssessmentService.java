package com.example.api.domain.assessments;

import com.example.api.domain.ValidationException;
import com.example.api.domain.adminsettings.AdminSettings;
import com.example.api.domain.assessmentfieldsvalues.AssessmentFieldsValues;
import com.example.api.domain.assessmentfieldsvalues.AssessmentFieldsValuesRegisterDTO;
import com.example.api.domain.fields.FieldType;
import com.example.api.domain.fieldsvalues.FieldValue;
import com.example.api.domain.fieldsvalues.FieldValueRegisterDTO;
import com.example.api.domain.gradings.Gradings;
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
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class AssessmentService {

    @Autowired
    private AssessmentRepository assessmentRepository;

    @Autowired
    private GradingRepository gradingRepository;

    @Autowired
    private AdminSettingRepository adminSettingRepository;

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

        // Flush changes to the database
        assessmentRepository.flush();
        inventoryItemsFieldValuesRepository.flush();

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
                        parentInventoryItem.getStorageLevel(),
                        "NA",
                        !component.pulled(), // if component was pulled, it should not be present in the inventory items
                        area,
                        component.serialNumber(),
                        "temp",
                        "Component",
                        BigDecimal.ZERO,
                        null
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
        parentInventoryItem.setSerialNumber(data.parentSerialNumber());

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

        // Flush changes to the database again
        inventoryItemRepository.flush();
        inventoryItemsFieldValuesRepository.flush();

        // Calculate grades of main item and its components
        calculateGrade(parentAssessment.getInventoryItem());
    }

    //    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private void calculateGrade(InventoryItem inventoryItem) {
        var minimumGrades = adminSettingRepository.findByService("minimum_grading")
                .stream()
                .collect(Collectors.toMap(AdminSettings::getKeyParam, s -> Long.valueOf(s.getValueParam())));

        var minimumCosmeticCriticalGrade = Optional.ofNullable(minimumGrades.get("cosmetic_critical"))
                .orElseThrow(() -> new NoSuchElementException("cosmetic_critical not found"));

        var minimumCosmeticNonCriticalGrade = Optional.ofNullable(minimumGrades.get("cosmetic_non_critical"))
                .orElseThrow(() -> new NoSuchElementException("cosmetic_non_critical not found"));

        var minimumFunctionalCriticalGrade = Optional.ofNullable(minimumGrades.get("functional_critical"))
                .orElseThrow(() -> new NoSuchElementException("functional_critical not found"));

        var minimumFunctionalNonCriticalGrade = Optional.ofNullable(minimumGrades.get("functional_non_critical"))
                .orElseThrow(() -> new NoSuchElementException("functional_non_critical not found"));

        AtomicReference<Long> lowestMainItemFunctionalScore = new AtomicReference<>(99L);
        AtomicReference<Long> lowestMainItemCosmeticScore = new AtomicReference<>(99L);

        // Query the database for scores
        Long functionalScore = inventoryItemsFieldValuesRepository.findMinScoreOfInventoryItem(inventoryItem.getId(), FieldType.FUNCTIONAL);
        Long cosmeticScore = inventoryItemsFieldValuesRepository.findMinScoreOfInventoryItem(inventoryItem.getId(), FieldType.COSMETIC);

        if (functionalScore != null) {
            lowestMainItemFunctionalScore.set(functionalScore);
        }

        if (cosmeticScore != null) {
            lowestMainItemCosmeticScore.set(cosmeticScore);
        }

        // Calculates the grades of the main item
        var mainItemInventoryItem = inventoryItemRepository.getReferenceById(inventoryItem.getId());
//        AtomicReference<Long> lowestMainItemFunctionalScore = new AtomicReference<>(
//                inventoryItemsFieldValuesRepository.findMinScoreOfInventoryItem(mainItemInventoryItem.getId(), FieldType.FUNCTIONAL)
//        );
//        AtomicReference<Long> lowestMainItemCosmeticScore = new AtomicReference<>(
//                inventoryItemsFieldValuesRepository.findMinScoreOfInventoryItem(mainItemInventoryItem.getId(), FieldType.COSMETIC)
//        );

        var mainItemComponentsInventoryItems = inventoryItemComponentRepository.findByParentInventoryItemId(inventoryItem.getId());

        // Calculates the grades of the components
        mainItemComponentsInventoryItems.forEach(item -> {
            Long functionalGrade = 0L;
            Long cosmeticGrade = 0L;

            var lowestFunctionalScore = inventoryItemsFieldValuesRepository.findMinScoreOfInventoryItem(item.getInventoryItem().getId(), FieldType.FUNCTIONAL);
            var lowestCosmeticScore = inventoryItemsFieldValuesRepository.findMinScoreOfInventoryItem(item.getInventoryItem().getId(), FieldType.COSMETIC);

            if (lowestFunctionalScore != null && lowestFunctionalScore < lowestMainItemFunctionalScore.get()) {
                lowestMainItemFunctionalScore.set(lowestFunctionalScore);
            }

            if (lowestCosmeticScore != null && lowestCosmeticScore < lowestMainItemCosmeticScore.get()) {
                lowestMainItemCosmeticScore.set(lowestCosmeticScore);
            }

            Boolean isCritical = item.getInventoryItem().getSectionArea().getIsCritical();

            if (lowestFunctionalScore == null) {
                functionalGrade = null;
            } else {
                var minFunctionalGrade = isCritical ? minimumFunctionalCriticalGrade : minimumFunctionalNonCriticalGrade;
                functionalGrade = lowestFunctionalScore < minFunctionalGrade ? minFunctionalGrade : lowestFunctionalScore;
            }

            if (lowestCosmeticScore == null) {
                cosmeticGrade = null;
            } else {
                var minCosmeticGrade = isCritical ? minimumCosmeticCriticalGrade : minimumCosmeticNonCriticalGrade;
                cosmeticGrade = lowestCosmeticScore < minCosmeticGrade ? minCosmeticGrade : lowestCosmeticScore;
            }

            var functionalGrading = Optional.ofNullable(gradingRepository.findByTypeAndScore("functional", functionalGrade))
                    .map(Gradings::getGrade)
                    .orElse("NA");

            var cosmeticGrading = Optional.ofNullable(gradingRepository.findByTypeAndScore("cosmetic", cosmeticGrade))
                    .map(Gradings::getGrade)
                    .orElse("NA");

            // Determine company grade
            String companyGrade;
            if ("FA".equals(functionalGrading)) {
                companyGrade = "FA";
            } else {
                companyGrade = Optional.ofNullable(gradingRepository.findByTypeAndScore("cosmetic", cosmeticGrade))
                        .map(Gradings::getCompany_grade)
                        .orElse("NA");
            }

            var componentInventoryItem = item.getInventoryItem();

            if ("NA".equals(cosmeticGrading)) {
                companyGrade = "U";
            }
            componentInventoryItem.setFunctionalGrade(functionalGrading);
            componentInventoryItem.setCosmeticGrade(cosmeticGrading);
            componentInventoryItem.setCompanyGrade(companyGrade);
        });

        // Determine company grade for main item
        String mainItemCompanyGrade;
        if (lowestMainItemFunctionalScore.get() == 0L) {
            mainItemCompanyGrade = "FA";
        } else {
            mainItemCompanyGrade = Optional.ofNullable(gradingRepository.findByTypeAndScore("cosmetic", lowestMainItemCosmeticScore.get()))
                    .map(Gradings::getCompany_grade)
                    .orElse("NA");
        }

        if (lowestMainItemFunctionalScore.get() == 99L && lowestMainItemCosmeticScore.get() == 99L) {
            mainItemCompanyGrade = "NA";
        }
        Gradings functionalGrading = gradingRepository.findByTypeAndScore("functional", lowestMainItemFunctionalScore.get());
        mainItemInventoryItem.setFunctionalGrade(
                functionalGrading != null ? functionalGrading.getCompany_grade() : "NA"
        );

        Gradings cosmeticGrading = gradingRepository.findByTypeAndScore("cosmetic", lowestMainItemCosmeticScore.get());
        mainItemInventoryItem.setCosmeticGrade(
                cosmeticGrading != null ? cosmeticGrading.getCompany_grade() : "NA"
        );
//            mainItemInventoryItem.setCosmeticGrade(gradingRepository.findByTypeAndScore("cosmetic", lowestMainItemCosmeticScore.get()).getGrade());

        mainItemInventoryItem.setCompanyGrade(mainItemCompanyGrade);
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
                        score = 5.0; // Ensure this matches the expected score for functional fields
                    }
                    var newFieldvalue = new FieldValue(new FieldValueRegisterDTO(valueData, score, specField));
                    fieldValue = fieldValueRepository.save(newFieldvalue);
                }

                // Save the field value to the inventory item
                var inventoryItemsFieldsValues = new InventoryItemsFieldsValues(new InventoryItemsFieldsValuesRegisterDTO(fieldValue, parentInventoryItem));
                inventoryItemsFieldValuesRepository.save(inventoryItemsFieldsValues);

                // Save the field value to the assessment
                var assessmentFieldsValues = new AssessmentFieldsValues(new AssessmentFieldsValuesRegisterDTO(fieldValue, parentAssessment));
                assessmentFieldValuesRepository.save(assessmentFieldsValues);
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
                AssessmentRequestInspectionDTO component = new AssessmentRequestInspectionDTO(spec.present(), spec.areaId(), spec.modelId(), spec.mpnId(), spec.pulled(), spec.isCritical(), spec.serialNumber(), spec.fields());
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
