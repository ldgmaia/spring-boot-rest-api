package com.example.api.domain.models;

import com.example.api.domain.ValidationException;
import com.example.api.domain.categories.CategoryInfoDTO;
import com.example.api.domain.modelfieldsvalues.ModelFieldValueInfoDTO;
import com.example.api.domain.modelfieldsvalues.ModelFieldValueRegisterDTO;
import com.example.api.domain.modelfieldsvalues.ModelFieldsValues;
import com.example.api.domain.sectionareas.SectionArea;
import com.example.api.domain.sectionareas.SectionAreaInfoDTO;
import com.example.api.domain.sectionareas.SectionAreaRegisterDTO;
import com.example.api.domain.sections.Section;
import com.example.api.domain.sections.SectionRegisterDTO;
import com.example.api.domain.sections.SectionWithAreasDTO;
import com.example.api.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ModelService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelRepository modelRepository;

    @Autowired
    private FieldValueRepository fieldValueRepository;

    @Autowired
    private ModelFieldValueRepository modelFieldValueRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private SectionAreaRepository sectionAreaRepository;

//    @Autowired
//    private List<FieldValidator> validators; // Spring boot will automatically detect that a List is being ejected and will get all classes that implements this interface and will inject the validators automatically


    public ModelInfoDTO register(ModelRequestDTO data) {
//        validators.forEach(v -> v.validate(data));
        try {
            var category = categoryRepository.getReferenceById(data.categoryId());

            var model = new Model(new ModelRegisterDTO(data.name(), data.description(), data.identifier(), data.needsMpn(), category));
            modelRepository.save(model);

            if (data.modelFieldsValues() != null) {
                data.modelFieldsValues()
                        .forEach(mfv -> {
                            var modelFieldValue = new ModelFieldsValues(new ModelFieldValueRegisterDTO(fieldValueRepository.findByFieldIdAndValueDataId(mfv.fieldId(), mfv.valueDataId()), model));
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

        List<ModelFieldValueInfoDTO> fields = modelFieldValueRepository.findFieldsValuesByModelId(id);

        // Assemble the final DTO
        return new ModelInfoDetailsDTO(
                model.getId(),
                model.getName(),
                model.getDescription(),
                model.getIdentifier(),
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
                        mfv -> new ModelFieldId(mfv.fieldId(), mfv.valueDataId()),
                        mfv -> new ModelFieldsValues(new ModelFieldValueRegisterDTO(
                                fieldValueRepository.findByFieldIdAndValueDataId(mfv.fieldId(), mfv.valueDataId()),
                                model
                        ))
                ));

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


//        // Remove old field values not present in the new data
//        for (var key : existingFieldValuesMap.keySet()) {
//            if (!newFieldValuesMap.containsKey(key)) {
//                modelFieldValueRepository.delete(existingFieldValuesMap.get(key));
//            }
//        }
//
//        // Add or keep new field values
//        for (var key : newFieldValuesMap.keySet()) {
//            if (!existingFieldValuesMap.containsKey(key)) {
//                modelFieldValueRepository.save(newFieldValuesMap.get(key));
//            }
//        }
//
//        // Handle sections and areas
//        Map<Long, Section> existingSectionsMap = sectionRepository.findAllByModelId(modelId).stream()
//                .collect(Collectors.toMap(Section::getId, section -> section));
//
//        var newSectionsMap = data.sections().stream()
//                .collect(Collectors.toMap(
////                        SectionDTO::id,
//                        s -> s,
//                        s -> new Section(new SectionRegisterDTO(s.name(), s.sectionOrder(), model))
//                ));
//
//        // Update or delete existing sections
//        for (var id : existingSectionsMap.keySet()) {
//            if (!newSectionsMap.containsKey(id)) {
//                sectionRepository.delete(existingSectionsMap.get(id));
//            } else {
//                Section section = existingSectionsMap.get(id);
//                var newSection = data.sections().stream().filter(s -> Objects.equals(s.id(), id)).findFirst().orElseThrow();
//                section.setName(newSection.name());
//                section.setSectionOrder(newSection.sectionOrder());
//
//                Map<Long, SectionArea> existingAreasMap = sectionAreaRepository.findAllBySectionId(section.getId()).stream()
//                        .collect(Collectors.toMap(SectionArea::getId, area -> area));
//
//                var newAreasMap = newSection.areas().stream()
//                        .collect(Collectors.toMap(
//                                sa -> sa,
//                                sa -> new SectionArea(new SectionAreaRegisterDTO(
//                                        sa.name(), section, sa.areaOrder(), sa.printOnLabel(),
//                                        sa.printAreaNameOnLabel(), sa.orderOnLabel(), sa.isCritical()
//                                ))
//                        ));
//
//                // Update or delete existing areas
//                for (var areaId : existingAreasMap.keySet()) {
//                    if (!newAreasMap.containsKey(areaId)) {
//                        sectionAreaRepository.delete(existingAreasMap.get(areaId));
//                    } else {
//                        SectionArea area = existingAreasMap.get(areaId);
//                        var newArea = newSection.areas().stream().filter(a -> Objects.equals(a.id(), areaId)).findFirst().orElseThrow();
//                        area.setName(newArea.name());
//                        area.setAreaOrder(newArea.areaOrder());
//                        area.setPrintOnLabel(newArea.printOnLabel());
//                        area.setPrintAreaNameOnLabel(newArea.printAreaNameOnLabel());
//                        area.setOrderOnLabel(newArea.orderOnLabel());
//                        area.setIsCritical(newArea.isCritical());
//                    }
//                }
//
//                // Add new areas
//                for (var areaId : newAreasMap.keySet()) {
//                    if (!existingAreasMap.containsKey(areaId)) {
//                        sectionAreaRepository.save(newAreasMap.get(areaId));
//                    }
//                }
//            }
//        }

        // Add new sections
//        for (var section : newSectionsMap.keySet()) {
//            if (!existingSectionsMap.containsKey(section)) {
//                sectionRepository.save(newSectionsMap.get(section));
//                sectionAreaRepository.findAllBySectionId(section.id()).forEach(sectionAreaRepository::save);
////                newSectionsMap.get(id).getAreas().forEach(sectionAreaRepository::save);
//            }
//        }

        modelRepository.save(model);
        return new ModelInfoDTO(model);
    }
//}

////    public Page<Field> getAllEnabledFieldsByFieldGroupId(Long fieldGroupId, Pageable pageable) {
////        return fieldRepository.findByEnabledTrueAndFieldGroup_Id(fieldGroupId, pageable);
////    }
//
//    public FieldsByGroupDTO getEnabledFieldsByFieldGroupId(Long fieldGroupId) {
//        var fieldGroup = fieldGroupRepository.findById(fieldGroupId).orElse(null);
//        if (fieldGroup == null) {
//            throw new ValidationException("Field Group ID not found");
//        }
//
//        List<Field> fields = fieldRepository.findByEnabledTrueAndFieldGroupId(fieldGroupId);
//        List<FieldListDTO> fieldsListDTO = fields.stream().map(FieldListDTO::new).collect(Collectors.toList());
//
//        return new FieldsByGroupDTO(fieldGroup.getName(), fieldsListDTO);
//    }
}
