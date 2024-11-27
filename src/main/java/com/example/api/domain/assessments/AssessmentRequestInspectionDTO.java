package com.example.api.domain.assessments;

import java.util.List;

public record AssessmentRequestInspectionDTO(
        Boolean present,
        Long areaId,
        Long modelId,
        Long mpnId,
        Boolean pulled,
        String serialNumber,
        List<AssessmentRequestFieldsDTO> fields
) {
}
