package com.example.api.domain.models;

import com.example.api.domain.ValidationException;
import com.example.api.domain.categories.CategoryInfoDTO;
import com.example.api.domain.fieldsvalues.FieldValue;
import com.example.api.domain.fieldsvalues.FieldValueRegisterDTO;
import com.example.api.domain.modelfieldsvalues.ModelFieldValueInfoDTO;
import com.example.api.domain.modelfieldsvalues.ModelFieldValueRegisterDTO;
import com.example.api.domain.modelfieldsvalues.ModelFieldsValues;
import com.example.api.domain.sectionareas.SectionArea;
import com.example.api.domain.sectionareas.SectionAreaInfoDTO;
import com.example.api.domain.sectionareas.SectionAreaRegisterDTO;
import com.example.api.domain.sections.Section;
import com.example.api.domain.sections.SectionRegisterDTO;
import com.example.api.domain.sections.SectionWithAreasDTO;
import com.example.api.domain.values.Value;
import com.example.api.domain.values.ValueRegisterDTO;
import com.example.api.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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
                                var newFieldValue = new FieldValue(new FieldValueRegisterDTO(valueData, null, fieldRepository.getReferenceById(mfv.fieldId())));
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

        List<SectionWithAreasDTO> sections = sectionRepository.findAllByModelId(id).stream().map(s -> {
            List<SectionAreaInfoDTO> areas = sectionAreaRepository.findSectionAreasBySectionId(s.getId());
            return new SectionWithAreasDTO(s, areas);
        }).collect(Collectors.toList());

//        List<ModelFieldValueInfoDTO> fields = modelFieldValueRepository.findFieldsValuesByModelId(id);
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
                                var newFieldValue = new FieldValue(new FieldValueRegisterDTO(valueData, null, fieldRepository.getReferenceById(mfv.fieldId())));
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
                                var newFieldValue = new FieldValue(new FieldValueRegisterDTO(valueData, null, fieldRepository.getReferenceById(mfv.fieldId())));
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
        Map<Long, Section> existingSectionsMap = sectionRepository.findAllByModelId(modelId).stream()
                .collect(Collectors.toMap(Section::getId, section -> section));

        // Create a map to hold new sections with their areas
        Map<Long, Section> newSectionsMap = new HashMap<>();
        data.sections().forEach(s -> {
            Section section = new Section(new SectionRegisterDTO(s.name(), s.sectionOrder(), model));
            if (s.id() != null) {
                section.setId(s.id()); // Ensure the section ID is set for existing sections
            }
            newSectionsMap.put(section.getId(), section);

            // Handle areas for the section
            List<SectionArea> areas = s.areas().stream()
                    .map(sa -> {
                        SectionArea area = new SectionArea(new SectionAreaRegisterDTO(
                                sa.name(), section, sa.areaOrder(), sa.printOnLabel(),
                                sa.printAreaNameOnLabel(), sa.orderOnLabel(), sa.isCritical()
                        ));
                        if (sa.id() != null) {
                            area.setId(sa.id()); // Ensure the area ID is set for existing areas
                        }
                        area.setSection(section); // Set the section for the area
                        return area;
                    }).collect(Collectors.toList());
            section.setAreas(areas);
        });

        // Update or delete existing sections
        for (var id : existingSectionsMap.keySet()) {
            if (!newSectionsMap.containsKey(id)) {
                sectionRepository.delete(existingSectionsMap.get(id));
            } else {
                Section section = existingSectionsMap.get(id);
                var newSection = data.sections().stream().filter(s -> Objects.equals(s.id(), id)).findFirst().orElseThrow();
                section.setName(newSection.name());
                section.setSectionOrder(newSection.sectionOrder());

                // Handle areas for the section
                Map<Long, SectionArea> existingAreasMap = sectionAreaRepository.findAllBySectionId(section.getId()).stream()
                        .collect(Collectors.toMap(SectionArea::getId, area -> area));

                Map<Long, SectionArea> newAreasMap = new HashMap<>();
                newSection.areas().forEach(sa -> {
                    SectionArea area = new SectionArea(new SectionAreaRegisterDTO(
                            sa.name(), section, sa.areaOrder(), sa.printOnLabel(),
                            sa.printAreaNameOnLabel(), sa.orderOnLabel(), sa.isCritical()
                    ));
                    if (sa.id() != null) {
                        area.setId(sa.id()); // Ensure the area ID is set for existing areas
                    }
                    area.setSection(section); // Set the section for the area
                    newAreasMap.put(area.getId(), area);
                });

                // Update or delete existing areas
                for (var areaId : existingAreasMap.keySet()) {
                    if (!newAreasMap.containsKey(areaId)) {
                        sectionAreaRepository.delete(existingAreasMap.get(areaId));
                    } else {
                        SectionArea area = existingAreasMap.get(areaId);
                        var newArea = newSection.areas().stream().filter(a -> Objects.equals(a.id(), areaId)).findFirst().orElseThrow();
                        area.setName(newArea.name());
                        area.setAreaOrder(newArea.areaOrder());
                        area.setPrintOnLabel(newArea.printOnLabel());
                        area.setPrintAreaNameOnLabel(newArea.printAreaNameOnLabel());
                        area.setOrderOnLabel(newArea.orderOnLabel());
                        area.setIsCritical(newArea.isCritical());
                    }
                }

                // Add new areas
                for (var areaId : newAreasMap.keySet()) {
                    if (!existingAreasMap.containsKey(areaId)) {
                        sectionAreaRepository.save(newAreasMap.get(areaId));
                    }
                }
            }
        }

        // Add new sections
        for (var section : newSectionsMap.values()) {
            if (!existingSectionsMap.containsKey(section.getId())) {
                sectionRepository.save(section);
                section.getAreas().forEach(sectionAreaRepository::save);
            }
        }

        modelRepository.save(model);
        return new ModelInfoDTO(model);
    }

    public List<ModelsByCategoryDTO> getModelsByCategoryId(Long categoryId) {
        return modelRepository.findAllByCategoryId(categoryId);
    }

}
