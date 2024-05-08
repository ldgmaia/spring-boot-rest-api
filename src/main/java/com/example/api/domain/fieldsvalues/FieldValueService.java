package com.example.api.domain.fieldsvalues;

import com.example.api.domain.ValidationException;
import com.example.api.domain.fieldsvalues.validations.FieldValueValidator;
import com.example.api.domain.values.Value;
import com.example.api.domain.values.ValueRegisterDTO;
import com.example.api.repositories.FieldRepository;
import com.example.api.repositories.FieldValueRepository;
import com.example.api.repositories.ValueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FieldValueService {

    @Autowired
    private FieldRepository fieldRepository;

    @Autowired
    private ValueRepository valueRepository;

    @Autowired
    private FieldValueRepository fieldValueRepository;

    @Autowired
    private List<FieldValueValidator> validators; // Spring boot will automatically detect that a List is being ejected and will get all classes that implements this interface and will inject the validators automatically

    public FieldValueInfoDTO register(FieldValueRequestDTO data) {
        validators.forEach(v -> v.validate(data));
//        var field = fieldRepository.getReferenceById(data.fieldId());
        var field = fieldRepository.existsById(data.fieldId());
        if (!field) {
            throw new ValidationException("Field ID not found");
        }

        var valueExists = valueRepository.existsByValueData(data.valueData());
        var value = valueExists ? valueRepository.findByValueData(data.valueData()) : new Value(new ValueRegisterDTO(data.valueData()));

//        Value value;
//        if (valueExists) {
//
//            value = valueRepository.findByValueData(data.valueData());
//        } else {
//            Value newValue = new Value(new ValueRegisterDTO(data.valueData()));
//
//            Value savedValue = valueRepository.save(newValue);
//
//            value = savedValue;
//        }

        var fieldvalue = new FieldValue(new FieldValueRegisterDTO(value, data.score(), fieldRepository.getReferenceById(data.fieldId())));
        fieldValueRepository.save(fieldvalue);

        return new FieldValueInfoDTO(fieldvalue);
    }

    public FieldValueListDTO findAllValuesByFieldId(Long id) {
        var field = fieldRepository.getReferenceById(id);
        var list = fieldValueRepository.findAllEnabledValuesByFieldId(id);

        return new FieldValueListDTO(field, list);
    }
}
