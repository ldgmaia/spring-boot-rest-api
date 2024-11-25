package com.example.api.domain.fields;

import com.example.api.domain.ValidationException;
import com.example.api.domain.fieldgroups.FieldGroup;
import com.example.api.domain.fields.validations.FieldValidator;
import com.example.api.domain.fieldsvalues.FieldValue;
import com.example.api.domain.fieldsvalues.FieldValueRegisterDTO;
import com.example.api.repositories.FieldGroupRepository;
import com.example.api.repositories.FieldRepository;
import com.example.api.repositories.FieldValueRepository;
import com.example.api.repositories.ValueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FieldService {

    @Autowired
    private FieldRepository fieldRepository;

    @Autowired
    private FieldValueRepository fieldValueRepository;

    @Autowired
    private FieldGroupRepository fieldGroupRepository;

    @Autowired
    private ValueRepository valueRepository;

    @Autowired
    private List<FieldValidator> validators; // Spring boot will automatically detect that a List is being ejected and will get all classes that implements this interface and will inject the validators automatically

    public FieldInfoDTO register(FieldRequestDTO data) {
        validators.forEach(v -> v.validate(data));

        FieldGroup fieldGroup = null;

        Long fieldGroupId = data.fieldGroupId();
        if (fieldGroupId != null) {
            if (!fieldGroupRepository.existsById(fieldGroupId)) {
                throw new ValidationException("Field Group not found");
            }
            fieldGroup = fieldGroupRepository.getReferenceById(fieldGroupId);
        }

        var field = new Field(new FieldRegisterDTO(data.name(), data.isMultiple(), data.dataType(), data.fieldType(), fieldGroup));

        fieldRepository.save(field);

        // If field DataType is BOOLEAN, it should create the fieldValue records for true and false automatically with the pre-existing valueData IDs.
        if (data.dataType() == DataType.BOOLEAN) {
            fieldValueRepository.save(new FieldValue(new FieldValueRegisterDTO(valueRepository.findByValueData("true"), null, field)));
            fieldValueRepository.save(new FieldValue(new FieldValueRegisterDTO(valueRepository.findByValueData("false"), null, field)));
        }
        return new FieldInfoDTO(field);
    }

    public FieldInfoDTO updateInfo(FieldUpdateDTO data, Long id) {

        // check if name is available
        var fieldNewInfo = fieldRepository.findByName(data.name());
        if (fieldNewInfo != null && !fieldNewInfo.getId().equals(id)) {
            throw new ValidationException("Field name already being used");
        }

        Field field = fieldRepository.getReferenceById(id);

        if (data.fieldGroupId() != null) {
            var fieldGroup = fieldGroupRepository.getReferenceById(data.fieldGroupId());
            field.setFieldGroup(fieldGroup);
        }

        field.setName(data.name());

        return new FieldInfoDTO(field);
    }

    public FieldsByGroupDTO getEnabledFieldsByFieldGroupId(Long fieldGroupId) {
        var fieldGroup = fieldGroupRepository.findById(fieldGroupId).orElse(null);
        if (fieldGroup == null) {
            throw new ValidationException("Field Group ID not found");
        }

        List<Field> fields = fieldRepository.findByEnabledTrueAndFieldGroupId(fieldGroupId);
        List<FieldListDTO> fieldsListDTO = fields.stream().map(FieldListDTO::new).collect(Collectors.toList());

        return new FieldsByGroupDTO(fieldGroup.getName(), fieldsListDTO);
    }
}
