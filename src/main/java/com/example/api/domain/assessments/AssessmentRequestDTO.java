package com.example.api.domain.assessments;

import java.util.List;

public record AssessmentRequestDTO(
        Long parentInventoryItemId,
        List<AssessmentRequestFieldsDTO> mainItemSpecs,
        List<AssessmentRequestFieldsDTO> mainItemFunctional,
        List<AssessmentRequestFieldsDTO> mainItemCosmetic,
        List<AssessmentRequestInspectionDTO> specs,
        List<AssessmentRequestInspectionDTO> functional,
        List<AssessmentRequestInspectionDTO> cosmetic
) {

}
