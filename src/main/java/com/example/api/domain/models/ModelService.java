package com.example.api.domain.models;

import com.example.api.domain.modelfieldsvalues.ModelFieldValueRegisterDTO;
import com.example.api.domain.modelfieldsvalues.ModelFieldsValues;
import com.example.api.domain.sectionareas.SectionArea;
import com.example.api.domain.sectionareas.SectionAreaRegisterDTO;
import com.example.api.domain.sections.Section;
import com.example.api.domain.sections.SectionRegisterDTO;
import com.example.api.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

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

            data.modelFieldsValues().stream()
                    .forEach(mfv -> {
                        var modelFieldValue = new ModelFieldsValues(new ModelFieldValueRegisterDTO(fieldValueRepository.findByFieldIdAndValueDataId(mfv.fieldId(), mfv.valueDataId()), model));
                        modelFieldValueRepository.save(modelFieldValue);
                    });

            data.sections().stream().forEach(s -> {
                var section = new Section(new SectionRegisterDTO(s.name(), s.sectionOrder(), model));
                sectionRepository.save(section);
                s.areas().stream().forEach(sa -> {
                    var sectionArea = new SectionArea(new SectionAreaRegisterDTO(sa.name(), section, sa.areaOrder(), sa.printOnLabel(), sa.printAreaNameOnLabel(), sa.orderOnLabel(), sa.isCritical()));
                    sectionAreaRepository.save(sectionArea);
                });

            });

            return new ModelInfoDTO(model);
        } catch (
                DataIntegrityViolationException ex) {
            throw new DataIntegrityViolationException("Database error: " + ex.getMostSpecificCause().getMessage());
//            throw new UniqueConstraintViolationException("A unique constraint was violated: " + ex.getMostSpecificCause().getMessage());
        }

    }

//    public FieldInfoDTO updateInfo(FieldUpdateDTO data) {
//
//        // check if name is available
//        var fieldNewInfo = fieldRepository.findByName(data.name());
//        if (!fieldNewInfo.getId().equals(data.id())) {
//            throw new ValidationException("Field name already being used");
//        }
//
//        Field field = fieldRepository.getReferenceById(data.id());
//
//        var fieldGroup = fieldGroupRepository.getReferenceById(data.fieldGroupId());
//
//        field.setName(data.name());
//        field.setFieldGroup(fieldGroup);
////        field.setFieldType(data.fieldType());
////        field.setDataType(data.dataType());
////        field.setUpdatedAt(LocalDateTime.now());
////        field.setIsMultiple(data.isMultiple() != null ? data.isMultiple() : false);
//
//        return new FieldInfoDTO(field);
//    }
//
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
