package com.example.api.domain.models;

import com.example.api.domain.modelfieldsvalues.ModelFieldValueRegisterDTO;
import com.example.api.domain.modelfieldsvalues.ModelFieldsValues;
import com.example.api.repositories.CategoryRepository;
import com.example.api.repositories.FieldValueRepository;
import com.example.api.repositories.ModelFieldValueRepository;
import com.example.api.repositories.ModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

//    @Autowired
//    private List<FieldValidator> validators; // Spring boot will automatically detect that a List is being ejected and will get all classes that implements this interface and will inject the validators automatically


    public ModelInfoDTO register(ModelRequestDTO data) {
//        validators.forEach(v -> v.validate(data));

        var category = categoryRepository.getReferenceById(data.categoryId());

        var model = new Model(new ModelRegisterDTO(data.name(), data.description(), data.identifier(), data.needsMpn(), category));
        modelRepository.save(model);
        
        data.modelFieldsValues().stream()
                .map(mfv -> {
                    var modelFieldValue = new ModelFieldsValues(new ModelFieldValueRegisterDTO(fieldValueRepository.findByFieldIdAndValueDataId(mfv.fieldId(), mfv.valueDataId()), model));
                    return modelFieldValueRepository.save(modelFieldValue);
                })
                .collect(Collectors.toList());

        return new ModelInfoDTO(model);
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
