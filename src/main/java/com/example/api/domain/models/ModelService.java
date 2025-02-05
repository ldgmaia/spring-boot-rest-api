package com.example.api.domain.models;

import com.example.api.domain.ValidationException;
import com.example.api.domain.categories.CategoryInfoDTO;
import com.example.api.domain.fieldsvalues.FieldValue;
import com.example.api.domain.fieldsvalues.FieldValueRegisterDTO;
import com.example.api.domain.modelfieldsvalues.ModelFieldValueInfoDTO;
import com.example.api.domain.modelfieldsvalues.ModelFieldValueRegisterDTO;
import com.example.api.domain.modelfieldsvalues.ModelFieldsValues;
import com.example.api.domain.sectionareamodels.SectionAreaModel;
import com.example.api.domain.sectionareamodels.SectionAreaModelRegisterDTO;
import com.example.api.domain.sectionareas.SectionArea;
import com.example.api.domain.sectionareas.SectionAreaInfoDTO;
import com.example.api.domain.sectionareas.SectionAreaRegisterDTO;
import com.example.api.domain.sections.Section;
import com.example.api.domain.sections.SectionRegisterDTO;
import com.example.api.domain.sections.SectionWithAreasDTO;
import com.example.api.domain.values.Value;
import com.example.api.domain.values.ValueRegisterDTO;
import com.example.api.infra.exception.UniqueConstraintViolationException;
import com.example.api.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ModelService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelRepository modelRepository;

    @Autowired
    private FieldRepository fieldRepository;

    @Autowired
    private FieldValueRepository fieldValueRepository;

    @Autowired
    private ModelFieldValueRepository modelFieldValueRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private SectionAreaRepository sectionAreaRepository;

    @Autowired
    private ValueRepository valueRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SectionAreaModelRepository sectionAreaModelRepository;

//    @Autowired
//    private List<ReceivingValidator> validators; // Spring boot will automatically detect that a List is being ejected and will get all classes that implements this interface and will inject the validators automatically

    public ModelInfoDTO register(ModelRequestDTO data) {
//        validators.forEach(v -> v.validate(data));
        try {
            var category = categoryRepository.getReferenceById(data.categoryId());

            // Fetch the currently logged-in user
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            var currentUser = userRepository.findByUsername(username);

            var model = new Model(new ModelRegisterDTO(data.name(), data.description(), data.identifier(), data.status(), data.needsMpn(), category), currentUser);
            modelRepository.save(model);

            if (data.modelFieldsValues() != null) {
                data.modelFieldsValues()
                        .forEach(mfv -> {
                            var valueData = valueRepository.findByValueData(mfv.valueData());
                            if (valueData == null) {
                                // Create and save new Value object if it doesn't exist
                                var newValueData = new Value(new ValueRegisterDTO(mfv.valueData()));
                                valueData = valueRepository.save(newValueData);
                            }

                            var mfvData = fieldValueRepository.findByFieldIdAndValueDataId(mfv.fieldId(), valueData.getId());
                            if (mfvData == null) {
                                // Create and save new FieldValue object if it doesn't exist
                                var field = fieldRepository.getReferenceById(mfv.fieldId());
                                Double score = null;
                                if (field.getFieldType().name().equals("COSMETIC")) {
                                    score = 9.0;
                                } else if (field.getFieldType().name().equals("FUNCTIONAL")) {
                                    score = 5.0;
                                }
                                var newFieldValue = new FieldValue(new FieldValueRegisterDTO(valueData, score, field));
                                mfvData = fieldValueRepository.save(newFieldValue);
                            }
                            var modelFieldValue = new ModelFieldsValues(new ModelFieldValueRegisterDTO(mfvData, model));
                            modelFieldValueRepository.save(modelFieldValue);
                        });
            }
            if (data.sections() != null) {
                data.sections().forEach(s -> {
                    var section = new Section(new SectionRegisterDTO(s.name(), s.sectionOrder(), model));
                    sectionRepository.save(section);

                    if (s.areas() != null) {
                        s.areas().forEach(sa -> {
                            var sectionArea = new SectionArea(new SectionAreaRegisterDTO(sa.name(), section, sa.areaOrder(), sa.printOnLabel(), sa.printAreaNameOnLabel(), sa.orderOnLabel(), sa.isCritical()));
                            sectionAreaRepository.save(sectionArea);

                            if (sa.models() != null) {
                                sa.models().forEach(sam -> {
                                    var sectionAreaModel = new SectionAreaModel(new SectionAreaModelRegisterDTO(sectionArea, modelRepository.getReferenceById(sam)));
                                    sectionAreaModelRepository.save(sectionAreaModel);
                                });
                            }
                        });
                    }
                });
            }

            return new ModelInfoDTO(model);
        } catch (
                DataIntegrityViolationException ex) {
            throw new DataIntegrityViolationException("Database error: " + ex.getMostSpecificCause().getMessage());
        }
    }

    public ModelInfoDetailsDTO getModelDetails(Long id) {
        // Fetch category details
        var model = modelRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Model not found"));

        // Fetch sections and areas with models
        List<SectionWithAreasDTO> sections = sectionRepository.findAllByModelIdOrderBySectionOrder(id).stream().map(s -> {
            List<SectionAreaInfoDTO> areas = sectionAreaRepository.findSectionAreasBySectionIdOrderByAreaOrder(s.getId()).stream().map(area -> {
                // Fetch model IDs for each area
                List<Long> modelIds = sectionAreaModelRepository.findModelIdsBySectionAreaId(area.id());

                // Replace the previous logic with the newly created repository call
//                List<SectionAreaModelInfoDTO> models = modelIds.stream()
//                        .map(SectionAreaModelInfoDTO::new)
//                        .collect(Collectors.toList());

                // Return area with models
                return new SectionAreaInfoDTO(area, modelIds);
            }).collect(Collectors.toList());

            return new SectionWithAreasDTO(s, areas);
        }).collect(Collectors.toList());

        // Fetch fields and sort by fieldId
        List<ModelFieldValueInfoDTO> fields = modelFieldValueRepository.findFieldsValuesByModelId(id).stream()
                .sorted(Comparator.comparingLong(ModelFieldValueInfoDTO::fieldId))  // Sort by fieldId
                .collect(Collectors.toList());

        // Assemble the final DTO
        return new ModelInfoDetailsDTO(
                model.getId(),
                model.getName(),
                model.getDescription(),
                model.getIdentifier(),
                model.getStatus(),
                model.getEnabled(),
                model.getNeedsMpn(),
                new CategoryInfoDTO(model.getCategory()),
                fields,
                sections
        );
    }

    public ModelInfoDTO update(ModelUpdateDTO data, Long modelId) {
        var model = modelRepository.findById(modelId).orElseThrow(() -> new RuntimeException("Model not found"));

        model.setName(data.name());
        model.setDescription(data.description());
        model.setIdentifier(data.identifier());
        model.setNeedsMpn(data.needsMpn());

        var category = categoryRepository.getReferenceById(data.categoryId());
        model.setCategory(category);

        // Handle model fields values
        Map<ModelFieldId, ModelFieldsValues> existingFieldValuesMap = modelFieldValueRepository.findAllByModelId(modelId).stream()
                .collect(Collectors.toMap(
                        mfv -> new ModelFieldId(mfv.getFieldValue().getField().getId(), mfv.getFieldValue().getValueData().getId()),
                        mfv -> mfv
                ));

        var newFieldValuesMap = data.modelFieldsValues().stream()
                .collect(Collectors.toMap(
                        mfv -> {

                            var valueData = valueRepository.findByValueData(mfv.valueData());
                            if (valueData == null) {
                                // Create and save new Value object if it doesn't exist
                                var newValueData = new Value(new ValueRegisterDTO(mfv.valueData()));
                                valueData = valueRepository.save(newValueData);
                            }

                            var mfvData = fieldValueRepository.findByFieldIdAndValueDataId(mfv.fieldId(), valueData.getId());
                            if (mfvData == null) {
                                // Create and save new FieldValue object if it doesn't exist
                                var field = fieldRepository.getReferenceById(mfv.fieldId());
                                Double score = null;
                                if (field.getFieldType().name().equals("COSMETIC")) {
                                    score = 9.0;
                                } else if (field.getFieldType().name().equals("FUNCTIONAL")) {
                                    score = 5.0;
                                }
                                var newFieldValue = new FieldValue(new FieldValueRegisterDTO(valueData, score, field));
                                mfvData = fieldValueRepository.save(newFieldValue);
                            }

                            return new ModelFieldId(mfvData.getField().getId(), mfvData.getValueData().getId());
                        },
                        mfv -> {
                            var valueData = valueRepository.findByValueData(mfv.valueData());
                            if (valueData == null) {
                                // Create and save new Value object if it doesn't exist
                                var newValueData = new Value(new ValueRegisterDTO(mfv.valueData()));
                                valueData = valueRepository.save(newValueData);
                            }

                            var mfvData = fieldValueRepository.findByFieldIdAndValueDataId(mfv.fieldId(), valueData.getId());
                            if (mfvData == null) {
                                // Create and save new FieldValue object if it doesn't exist
                                var field = fieldRepository.getReferenceById(mfv.fieldId());
                                Double score = null;
                                if (field.getFieldType().name().equals("COSMETIC")) {
                                    score = 9.0;
                                } else if (field.getFieldType().name().equals("FUNCTIONAL")) {
                                    score = 5.0;
                                }
                                var newFieldValue = new FieldValue(new FieldValueRegisterDTO(valueData, score, field));
                                mfvData = fieldValueRepository.save(newFieldValue);
                            }

                            return new ModelFieldsValues(new ModelFieldValueRegisterDTO(mfvData, model));
                        }
                ));

        // Remove old field values not present in the new data
        for (var key : existingFieldValuesMap.keySet()) {
            if (!newFieldValuesMap.containsKey(key)) {
                modelFieldValueRepository.delete(existingFieldValuesMap.get(key));
            }
        }

        // Add or keep new field values
        for (var key : newFieldValuesMap.keySet()) {
            if (!existingFieldValuesMap.containsKey(key)) {
                modelFieldValueRepository.save(newFieldValuesMap.get(key));
            }
        }

        // Handle sections and areas
        // Create a map to hold existing sections
        Map<Long, Section> existingSectionsMap = sectionRepository.findAllByModelIdOrderBySectionOrder(modelId).stream()
                .collect(Collectors.toMap(Section::getId, section -> section));

        // Create a map to hold new sections with their areas
        Map<Long, Section> newSectionsMap = new HashMap<>();
        data.sections().forEach(s -> {
            Section section;
            // For each new section, save it to the database if not already saved
            if (s.id() != null) {
                // Existing section - update it
                section = existingSectionsMap.get(s.id());
                section.setName(s.name());
                section.setSectionOrder(s.sectionOrder());
            } else {
                // New section - create and save it
                section = new Section(new SectionRegisterDTO(s.name(), s.sectionOrder(), model));
                sectionRepository.save(section);  // Save the section
                section = sectionRepository.findById(section.getId()).orElseThrow(); // Ensure section is persisted and fetched
            }
            newSectionsMap.put(section.getId(), section);

            // Create a final copy of section for use in lambdas
            final Section finalSection = section;

            // Handling areas within a section
            Map<Long, SectionArea> existingAreasMap = finalSection.getAreas().stream()
                    .collect(Collectors.toMap(SectionArea::getId, area -> area));

            // Handle areas for the section
            Map<Long, SectionArea> newAreasMap = new HashMap<>();
            s.areas().forEach(sa -> {
                SectionArea area;

                if (sa.id() != null && existingAreasMap.containsKey(sa.id())) {
                    // Update existing area
                    area = existingAreasMap.get(sa.id());
                    area.setName(sa.name());
                    area.setAreaOrder(sa.areaOrder());
                    area.setPrintOnLabel(sa.printOnLabel());
                    area.setPrintAreaNameOnLabel(sa.printAreaNameOnLabel());
                    area.setOrderOnLabel(sa.orderOnLabel());
                    area.setIsCritical(sa.isCritical());
                } else {
                    // Check if an area with the same name and section already exists to avoid duplicates
                    Optional<SectionArea> existingAreaOpt = sectionAreaRepository.findBySectionIdAndName(finalSection.getId(), sa.name());
                    if (existingAreaOpt.isPresent()) {
                        throw new UniqueConstraintViolationException(MessageFormat.format("Duplicate area \"{0}\" in the section \"{1}\"", sa.name(), finalSection.getName()));
                    } else {
                        // Create new area
                        area = new SectionArea(new SectionAreaRegisterDTO(
                                sa.name(), finalSection, sa.areaOrder(), sa.printOnLabel(),
                                sa.printAreaNameOnLabel(), sa.orderOnLabel(), sa.isCritical()
                        ));
                    }
                }

                sectionAreaRepository.save(area);

                newAreasMap.put(area.getId(), area);
                // Handle models for each area
                List<Long> newModelIds = sa.models(); // Model IDs from JSON

                // Fetch existing models for the current area from the section_areas_models table
                List<SectionAreaModel> existingModels = sectionAreaModelRepository.findBySectionAreaId(area.getId());
                Map<Long, SectionAreaModel> existingModelMap = existingModels.stream()
                        .collect(Collectors.toMap(sam -> sam.getModel().getId(), sam -> sam));

                // Remove any models that are no longer in the new list
                for (var existingModel : existingModels) {
                    if (!newModelIds.contains(existingModel.getModel().getId())) {
                        sectionAreaModelRepository.delete(existingModel);
                    }
                }

                // Add new models that are not in the existing models
                for (Long newModelId : newModelIds) {
                    if (!existingModelMap.containsKey(newModelId)) {
                        Model newModel = modelRepository.findById(newModelId)
                                .orElseThrow(() -> new RuntimeException("Model not found with id: " + newModelId));

                        SectionAreaModel newSectionAreaModel = new SectionAreaModel(
                                new SectionAreaModelRegisterDTO(area, newModel)
                        );
                        sectionAreaModelRepository.save(newSectionAreaModel);
                    }
                }
            });

            // Handle additions and updates in-place
            final List<SectionArea> currentAreas = finalSection.getAreas();

            // Remove areas that are no longer present
            currentAreas.removeIf(existingArea -> !newAreasMap.containsKey(existingArea.getId()));

            // Add or update areas in the section
            newAreasMap.forEach((areaId, newArea) -> {
                if (!currentAreas.contains(newArea)) {
                    currentAreas.add(newArea); // Add new area
                }
            });
            sectionRepository.save(finalSection);
        });

        // Delete existing sections
        for (var id : existingSectionsMap.keySet()) {
            if (!newSectionsMap.containsKey(id)) {
                sectionRepository.delete(existingSectionsMap.get(id));
            }
        }

        modelRepository.save(model);
        return new ModelInfoDTO(model);
    }

    public List<ModelsByCategoryDTO> getModelsByCategoryId(Long categoryId) {
        return modelRepository.findAllByCategoryId(categoryId);
    }
}
