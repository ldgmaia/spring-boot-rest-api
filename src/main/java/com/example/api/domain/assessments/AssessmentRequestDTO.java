package com.example.api.domain.assessments;

import java.util.List;

public record AssessmentRequestDTO(
        Long parentInventoryItemId,
        List<AssessmentRequestInspectionDTO> specs,
        List<AssessmentRequestInspectionDTO> functional,
        List<AssessmentRequestInspectionDTO> cosmetic
) {

}
