package com.example.api.domain.fieldsvalues.validations;

import com.example.api.domain.fieldsvalues.FieldValueRequestDTO;

public interface FieldValueValidator {

    void validate(FieldValueRequestDTO data);
}
