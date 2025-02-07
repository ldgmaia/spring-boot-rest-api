package com.example.api.domain.assessments;

import java.util.List;

public record AssessmentRequestInspectionDTO(
        Boolean present,
        Long areaId,
        Long modelId,
        Long mpnId,
        Boolean pulled,
        Boolean isCritical,
        String serialNumber,
        List<AssessmentRequestFieldsDTO> fields
) {
    public AssessmentRequestInspectionDTO(
            Boolean present,
            Long areaId,
            Long modelId,
            Long mpnId,
            Boolean pulled,
            Boolean isCritical,
            String serialNumber,
            List<AssessmentRequestFieldsDTO> fields
    ) {
        this.present = present;
        this.areaId = areaId;
        this.modelId = modelId;
        this.mpnId = mpnId;
        this.pulled = pulled != null ? pulled : false;
        this.isCritical = isCritical != null ? isCritical : false;
        this.serialNumber = serialNumber;
        this.fields = fields != null ? fields : List.of();
    }
}
