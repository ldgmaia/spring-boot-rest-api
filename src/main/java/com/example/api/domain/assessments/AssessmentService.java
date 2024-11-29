package com.example.api.domain.assessments;

import com.example.api.domain.ValidationException;
import com.example.api.domain.assessmentfieldsvalues.AssessmentFieldsValues;
import com.example.api.domain.assessmentfieldsvalues.AssessmentFieldsValuesRegisterDTO;
import com.example.api.domain.fieldsvalues.FieldValue;
import com.example.api.domain.fieldsvalues.FieldValueRegisterDTO;
import com.example.api.domain.inventoryitems.InventoryItem;
import com.example.api.domain.inventoryitems.InventoryItemRegisterDTO;
import com.example.api.domain.inventoryitemsfieldsvalues.InventoryItemsFieldsValues;
import com.example.api.domain.inventoryitemsfieldsvalues.InventoryItemsFieldsValuesRegisterDTO;
import com.example.api.domain.values.Value;
import com.example.api.domain.values.ValueRegisterDTO;
import com.example.api.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AssessmentService {

    @Autowired
    private AssessmentRepository assessmentRepository;

    @Autowired
    private InventoryItemRepository inventoryItemRepository;

    @Autowired
    private SectionAreaRepository sectionAreaRepository;

    @Autowired
    private SectionAreaModelRepository sectionAreaModelRepository;

    @Autowired
    private AssessmentFieldValuesRepository assessmentFieldValuesRepository;

    @Autowired
    private UserRepository userRepository;

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

//    public AssessmentListSpecFuncFieldsDTO findSpecFuncFieldsByInventoryItemId(Long inventoryItemId) {
//
//        List<SectionInfoDetailsDTO> sections = inventoryItemRepository.listSectionsByInventoryItemId(inventoryItemId);
//
//        System.out.println("SECTIONS ->>>>>>>>>>");
//        sections.stream().forEach(section -> {
//            System.out.println(section.name());
//            List<SectionAreaInfoDTO> areas = sectionAreaRepository.findSectionAreasBySectionIdOrderByAreaOrder(section.id());
//            areas.stream().forEach(area -> {
//                System.out.println(area.name());
//                System.out.println("AREAS ->>>>>>>>>>" + area);
//                List<Long> modelIds = sectionAreaModelRepository.findModelIdsBySectionAreaId(area.id());
//                List<Model> models = modelRepository.findByIdIn(modelIds);
//                List<ModelInfoDTO> modelInfoDTOS = models.stream().map(ModelInfoDTO::new).toList();
//                System.out.println("MODELS ->>>>>>>>>>");
//                modelInfoDTOS.stream().forEach(model -> {
//                    System.out.println(model);
//                });
//
//            });
//        });
//
////            areas.stream().map(area -> {
////                List<Long> modelIds = sectionAreaModelRepository.findModelIdsBySectionAreaId(area.id());
////                List<Model> models = modelRepository.findModelsByIds(modelIds);
////                System.out.println("AREAS ->>>>>>>>>>" + new SectionAreaInfoDTO(area, modelIds, models));
////                return new SectionAreaInfoDTO(area, modelIds, models);
////            });
//
////        List<SectionAreaInfoDTO> areas = new ArrayList<>();
////        for (SectionInfoDetailsDTO section : sections) {
////            areas.addAll(sectionAreaRepository.findSectionAreasBySectionIdOrderByAreaOrder(section.id()));
////        }
//
////        List<SectionAreaInfoDTO> areasWithModels = new ArrayList<>();
////        for (SectionAreaInfoDTO area : areas) {
////            // Retrieve model IDs for the current area
////            List<Long> modelIds = sectionAreaModelRepository.findModelIdsBySectionAreaId(area.id());
////            List<Model> models = modelRepository.findModelsByIds(modelIds);
////
////            // Use the constructor with models
////            SectionAreaInfoDTO areaWithModels = new SectionAreaInfoDTO(area, modelIds, models);
////            areasWithModels.add(areaWithModels);
////        }
//

    /// /        List<Object[]> queryResults = assessmentRepository.getSpecsByInventoryItemId(inventoryItemId);
    /// /
    /// /        List<SectionInfoDTO> sections = new ArrayList<>();
    /// /        List<SectionAreaInfoDTO> areas = new ArrayList<>();
    /// /        List<MPNInfoDTO> mpns = new ArrayList<>();
    /// /
    /// /        for (Object[] row : queryResults) {
    /// /            Long sectionId = (Long) row[0];
    /// /            String sectionName = (String) row[1];
    /// /            Long sectionAreaId = (Long) row[2];
    /// /            String sectionAreaName = (String) row[3];
    /// /            Long modelId = (Long) row[4];
    /// /            String modelName = (String) row[5];
    /// /            Long mpnId = (Long) row[6];
    /// /            String mpnName = (String) row[7];
    /// /            Long mpnModelId = (Long) row[8];
    /// /
    /// /            SectionInfoDTO section = new SectionInfoDTO(sectionId, sectionName, null);
    /// /            sections.add(section);
    /// /
    /// /            SectionAreaInfoDTO area = new SectionAreaInfoDTO(sectionAreaId, sectionAreaName, null, null, null, null, null, List.of(modelId));
    /// /            areas.add(area);
    /// /
    /// /            MPNInfoDTO mpn = new MPNInfoDTO(mpnId, mpnName, null, null, null);
    /// /            mpns.add(mpn);
    /// /
    /// /        }
//
//        return new AssessmentListSpecFuncFieldsDTO(sections);
//    }


//    public Assessment createAssessment(AssessmentRequestDTO request) {
//        request.specs().forEach(detail -> saveAssessmentDetail(detail, request));
//        request.functional().forEach(detail -> saveAssessmentDetail(detail, request));
//        request.cosmetic().forEach(detail -> saveAssessmentDetail(detail, request));
//
//        return assessmentRepository.findTopByOrderByIdDesc();
//    }
    public void saveDraftAssessment(AssessmentRequestDTO request) {

    }

    public void createAssessment(AssessmentRequestDTO data) {

        var parentInventoryItem = inventoryItemRepository.getReferenceById(data.parentInventoryItemId());

        processAssessment(data.specs(), parentInventoryItem);
        processAssessment(data.functional(), parentInventoryItem);
        processAssessment(data.cosmetic(), parentInventoryItem);

//        data.specs().forEach(spec -> {
//            var area = sectionAreaRepository.getReferenceById(spec.areaId());
//
//            InventoryItem inventoryItem;
//            if (spec.present()) {
//                var model = modelRepository.getReferenceById(spec.modelId());
//                inventoryItem = new InventoryItem(new InventoryItemRegisterDTO(
//                        modelRepository.getReferenceById(spec.modelId()).getCategory(),
//                        model,
//                        spec.mpnId() != null ? mpnRepository.getReferenceById(spec.mpnId()) : null,
//                        parentInventoryItem.getItemCondition(),
//                        parentInventoryItem.getItemStatus(),
//                        parentInventoryItem.getReceivingItem(),
//                        parentInventoryItem.getLocation(),
//                        "NA",
//                        true,
//                        area,
//                        spec.serialNumber(),
//                        "RBID TBD",
//                        "Component",
//                        BigDecimal.ZERO
//                ));
//                inventoryItemRepository.save(inventoryItem);
//            } else {
//                inventoryItem = null;
//            }
//
//            var assessment = new Assessment(new AssessmentRegisterDTO(
//                    spec.pulled() != null ? spec.pulled() : false,
//                    spec.present(),
//                    "status",
//                    null,
//                    null,
//                    null,
//                    area,
//                    parentInventoryItem,
//                    inventoryItem
//            ));
//            assessmentRepository.save(assessment);
//
//            if (spec.present()) {
//                spec.fields().forEach(field -> {
//                    if (field.fieldId() == null) {
//                        throw new ValidationException("Field ID is required for " + field);
//                    }
//
//                    var specField = fieldRepository.getReferenceById(field.fieldId());
//
//                    Value valueData;
//
//                    // Case 1: Both valueData and valueDataId are null
//                    if (field.valueDataId() == null && (field.valueData() == null || field.valueData().isEmpty())) {
//                        throw new ValidationException("Value is required for " + field);
//                    }
//
//                    // Case 2: valueDataId exists
//                    if (field.valueDataId() != null) {
//                        valueData = valueRepository.getReferenceById(field.valueDataId());
//                    } else {
//                        // Case 3: valueData provided, valueDataId must be null
//                        if (valueRepository.existsByValueData(field.valueData())) {
//                            valueData = valueRepository.findByValueData(field.valueData());
//                        } else {
//                            valueData = valueRepository.save(new Value(new ValueRegisterDTO(field.valueData())));
//                        }
//                    }
//
//                    // check if field value already exists
//                    FieldValue fieldValue;
//                    if (fieldValueRepository.existsByValuesDataIdAndFieldsId(valueData.getId(), specField.getId())) {
//                        fieldValue = fieldValueRepository.findByFieldIdAndValueDataId(specField.getId(), valueData.getId());
//                    } else {
//                        var newFieldvalue = new FieldValue(new FieldValueRegisterDTO(valueData, (double) 0, specField));
//                        fieldValue = fieldValueRepository.save(newFieldvalue);
//                    }
//
//                    var assessmentFieldsValues = new AssessmentFieldsValues(new AssessmentFieldsValuesRegisterDTO(fieldValue, assessment));
//                    assessmentFieldValuesRepository.save(assessmentFieldsValues);
//
//                    var inventoryItemsFieldsValues = new InventoryItemsFieldsValues(new InventoryItemsFieldsValuesRegisterDTO(fieldValue, inventoryItem));
//                    inventoryItemsFieldValuesRepository.save(inventoryItemsFieldsValues);
//                });
//            }
//        });
    }

    private void processAssessment(List<AssessmentRequestInspectionDTO> data, InventoryItem parentInventoryItem) {
        data.forEach(spec -> {
            var area = sectionAreaRepository.getReferenceById(spec.areaId());

            InventoryItem inventoryItem;
            if (spec.present()) {
                var model = modelRepository.getReferenceById(spec.modelId());
                inventoryItem = new InventoryItem(new InventoryItemRegisterDTO(
                        modelRepository.getReferenceById(spec.modelId()).getCategory(),
                        model,
                        spec.mpnId() != null ? mpnRepository.getReferenceById(spec.mpnId()) : null,
                        parentInventoryItem.getItemCondition(),
                        parentInventoryItem.getItemStatus(),
                        parentInventoryItem.getReceivingItem(),
                        parentInventoryItem.getLocation(),
                        "NA",
                        true,
                        area,
                        spec.serialNumber(),
                        "RBID TBD",
                        "Component",
                        BigDecimal.ZERO
                ));
                inventoryItemRepository.save(inventoryItem);
            } else {
                inventoryItem = null;
            }

            var assessment = new Assessment(new AssessmentRegisterDTO(
                    spec.pulled() != null ? spec.pulled() : false,
                    spec.present(),
                    "status",
                    null,
                    null,
                    null,
                    area,
                    parentInventoryItem,
                    inventoryItem
            ));
            assessmentRepository.save(assessment);

            if (spec.present()) {
                spec.fields().forEach(field -> {
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
                        var newFieldvalue = new FieldValue(new FieldValueRegisterDTO(valueData, (double) 0, specField));
                        fieldValue = fieldValueRepository.save(newFieldvalue);
                    }

                    var assessmentFieldsValues = new AssessmentFieldsValues(new AssessmentFieldsValuesRegisterDTO(fieldValue, assessment));
                    assessmentFieldValuesRepository.save(assessmentFieldsValues);

                    var inventoryItemsFieldsValues = new InventoryItemsFieldsValues(new InventoryItemsFieldsValuesRegisterDTO(fieldValue, inventoryItem));
                    inventoryItemsFieldValuesRepository.save(inventoryItemsFieldsValues);
                });
            }
        });
    }

//    private void saveAssessmentDetail(AssessmentRequestInspectionDTO data, AssessmentRequestDTO request) {
//// Fetch the currently logged-in user
//
////    pulled
////    present
////    status
////    companyGrade
////    cosmeticGrade
////    functionalGrade
////    parentInventoryItem
////    inventoryItem
//
//        // Save the assessment
////        Assessment assessment = new Assessment(new AssessmentRegisterDTO(
////                data.pulled(),
////                data.present(),
////                data.status(),
////                null,
////                null,
////                null,
////                request.parentInventoryItemId()
////                data.inventoryItem()
//////                null,
//////                data.p,
//////                data.itemCondition(),
//////                request.receivingItem(),
//////                data.companyGrade(),
//////                data.location(),
//////                data.mainRbid(),
//////                data.serialNumber(),
//////                data.pulled(),
//////                data.present(),
//////                data.cosmeticGrade(),
//////                data.functionalGrade(),
//////                data.lastInspectedBy(),
//////                data.mpnId(),
//////                data.sectionArea(),
//////                data.modelsId(),
//////                data.parentInventoryItemId(),
////                currentUser
////        ));
////        assessment.setParentInventoryItemsId(request.parentInventoryItemId());
//////        assessment.setReceivingItemId(request.inventoryItemId());
////        assessment.setPulled(detail.pulled());
////        assessment.setPresent(detail.present());
////        assessment.setModel(detail.modelId());
////        assessment.setMpn(detail.mpnId());
////        assessment.setSerialNumber(detail.serialNumber());
////        assessment.setCreatedBy(currentUser);
////        assessment.setCreatedAt(LocalDateTime.now());
////        assessment.setUpdatedAt(LocalDateTime.now());
//////        assessment.setMainRbid(String.valueOf(request.inventoryItemId()));
////        assessment.setSectionArea(detail.modelId());
////        assessment.setStatus("default_status");
////
////        Assessment savedAssessment = assessmentRepository.save(assessment);
//
//        // Handle fields
//        data.fields().forEach(field -> {
//
//            Long valueDataId = null;
//
//            // Case 1: Both valueData and valueDataId are null
//            if (field.valueDataId() == null && (field.valueData() == null || field.valueData().isEmpty())) {
//                throw new ValidationException("Both valueData and valueDataId cannot be null in: " + field);
//            }
//
//            // Case 2: valueDataId exists
//            if (field.valueDataId() != null) {
//                if (field.valueData() != null) {
//                    throw new IllegalArgumentException("valueData must be null when valueDataId is provided.");
//                }
//                valueDataId = Long.valueOf(field.valueDataId());
//
//            } else {
//                // Case 3: valueData provided, valueDataId must be null
//                if (valueRepository.existsByValueData(field.valueData())) {
//                    // Fetch the existing valueDataId
//                    Value existingValue = valueRepository.findByValueData(field.valueData());
//                    valueDataId = existingValue.getId();
//                } else {
//                    // Create and save a new Value
//                    Value newValue = new Value(new ValueRegisterDTO(field.valueData()));
//                    Value savedValue = valueRepository.save(newValue);
//                    valueDataId = savedValue.getId();
//                }
//            }
//
//            // Case 4: Both valueData and valueDataId provided
//            if (field.valueDataId() != null && field.valueData() != null) {
//                throw new IllegalArgumentException("Both valueData and valueDataId cannot be provided simultaneously.");
//            }
//
//            // Save the field value
//            AssessmentFieldsValues fieldValue = new AssessmentFieldsValues();
////            fieldValue.setAssessmentId(savedAssessment.getId());
//            fieldValue.setFieldValuesId(Long.valueOf(field.fieldId()));
//            fieldValue.setCreatedAt(LocalDateTime.now());
//            fieldValue.setUpdatedAt(LocalDateTime.now());
//
//            assessmentFieldValuesRepository.save(fieldValue);
//        });
//    }

//    public List<Assessment> getAllAssessments() {
//        return assessmentRepository.findAll();
//    }

//    private void validateSerialNumber(String serialNumber) {
//        if (serialNumber == null || serialNumber.isEmpty()) {
//            throw new ValidationException("Serial number cannot be null or empty.");
//        }
//
//        Boolean exists = inventoryItemRepository.existsBySerialNumber(serialNumber);
//        if (!exists) {
//            throw new ValidationException("Serial number " + serialNumber + " Doesn't exists.");
//        }
//    }
}
