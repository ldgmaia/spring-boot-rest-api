package com.example.api.domain.assessments;

public record AssessmentRequestFieldsDTO(
        Long fieldId,
        Long valueDataId,
        String valueData
) {
}
