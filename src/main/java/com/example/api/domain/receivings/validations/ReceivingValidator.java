package com.example.api.domain.receivings.validations;

import com.example.api.domain.receivings.ReceivingRequestDTO;

public interface ReceivingValidator {

    void validate(ReceivingRequestDTO data);
}
