package com.example.api.domain.fieldsvalues.validations;

import com.example.api.domain.ValidationException;
import com.example.api.domain.fieldsvalues.FieldValueRequestDTO;
import com.example.api.repositories.FieldValueRepository;
import com.example.api.repositories.ValueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DuplicateFieldValues implements FieldValueValidator {

    @Autowired
    private FieldValueRepository fieldValueRepository;

    @Autowired
    private ValueRepository valueRepository;

//    public void validate(FieldRequestDTO data) {
//
//        var fieldNameFound = fieldRepository.existsByName(data.name());
//
//        if (fieldNameFound) {
//            throw new ValidationException("Field name already registered");
//        }
//
//    }

    @Override
    public void validate(FieldValueRequestDTO data) {

        var value = valueRepository.findByValueData(data.valueData());
        if (value == null) {
            return;
        }
        var fieldValueExists = fieldValueRepository.existsByValuesDataIdAndFieldsId(value.getId(), data.fieldId());

        if (fieldValueExists) {
            throw new ValidationException("Field value already registered");
        }

    }
}
