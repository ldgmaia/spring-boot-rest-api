package com.example.api.domain.fields.validations;

import com.example.api.domain.ValidationException;
import com.example.api.domain.fields.FieldRequestDTO;
import com.example.api.repositories.FieldRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DuplicateFields implements FieldValidator {

    @Autowired
    private FieldRepository fieldRepository;

    public void validate(FieldRequestDTO data) {

        var fieldNameFound = fieldRepository.existsByName(data.name());

        if (fieldNameFound) {
            throw new ValidationException("Field name already registered");
        }

    }
}
