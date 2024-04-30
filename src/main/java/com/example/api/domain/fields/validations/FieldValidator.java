package com.example.api.domain.fields.validations;

import com.example.api.domain.fields.FieldRequestDTO;

public interface FieldValidator {

    void validate(FieldRequestDTO data);
}
