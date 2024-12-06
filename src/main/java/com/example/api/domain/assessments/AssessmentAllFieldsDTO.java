package com.example.api.domain.assessments;

import java.util.List;

public record AssessmentAllFieldsDTO(
        Long parentInventoryItemId,
        List<AssessmentRequestInspectionDTO> components
) {

}
