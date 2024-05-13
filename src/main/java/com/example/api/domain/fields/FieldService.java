package com.example.api.domain.fields;

import com.example.api.domain.ValidationException;
import com.example.api.domain.fields.validations.FieldValidator;
import com.example.api.repositories.FieldGroupRepository;
import com.example.api.repositories.FieldRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FieldService {

    @Autowired
    private FieldRepository fieldRepository;

    @Autowired
    private FieldGroupRepository fieldGroupRepository;

    @Autowired
    private List<FieldValidator> validators; // Spring boot will automatically detect that a List is being ejected and will get all classes that implements this interface and will inject the validators automatically

    public FieldInfoDTO register(FieldRequestDTO data) {

        validators.forEach(v -> v.validate(data));

        if (!fieldGroupRepository.existsById(data.fieldGroupId())) {
            throw new ValidationException("Field Group ID not found");
        }

        var fieldGroup = fieldGroupRepository.getReferenceById(data.fieldGroupId());

        var field = new Field(new FieldRegisterDTO(data.name(), data.isMultiple(), data.dataType(), data.fieldType(), fieldGroup));
        fieldRepository.save(field);

        return new FieldInfoDTO(field);
    }

    public FieldInfoDTO updateInfo(FieldUpdateDTO data) {

        Field field = fieldRepository.getReferenceById(data.id());
        var fieldGroup = fieldGroupRepository.getReferenceById(data.fieldGroupId());

        field.setName(data.name());
        field.setFieldType(data.fieldType());
//        field.setDataType(data.dataType());
        field.setUpdatedAt(LocalDateTime.now());
        field.setFieldGroup(fieldGroup);
        field.setIsMultiple(data.isMultiple() != null ? data.isMultiple() : false);

        return new FieldInfoDTO(field);
    }

//    public Page<Field> getAllEnabledFieldsByFieldGroupId(Long fieldGroupId, Pageable pageable) {
//        return fieldRepository.findByEnabledTrueAndFieldGroup_Id(fieldGroupId, pageable);
//    }

    public FieldsByGroupDTO getEnabledFieldsByFieldGroupId(Long fieldGroupId) {
        var fieldGroup = fieldGroupRepository.findById(fieldGroupId).orElse(null);
        if (fieldGroup == null) {
            throw new ValidationException("Field Group ID not found");
        }

        List<Field> fields = fieldRepository.findByEnabledTrueAndFieldGroup_Id(fieldGroupId);
        List<FieldListDTO> fieldsListDTO = fields.stream().map(FieldListDTO::new).collect(Collectors.toList());
        
        return new FieldsByGroupDTO(fieldGroup.getName(), fieldsListDTO);
    }
}
