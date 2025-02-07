package com.example.api.domain.sectionareas;

import com.example.api.domain.models.ModelAssessmentInfoDTO;

import java.util.List;

public record SectionAreaAssessmentInfoDTO(
        Long id,
        String name,
        Long areaOrder,
        Boolean isCritical,
        List<ModelAssessmentInfoDTO> models
) {
    public SectionAreaAssessmentInfoDTO(SectionArea sectionArea) {
        this(
                sectionArea.getId(),
                sectionArea.getName(),
                sectionArea.getAreaOrder(),
                sectionArea.getIsCritical(),
                sectionArea.getSectionAreaModels().stream().map(sectionAreaModel -> new ModelAssessmentInfoDTO(sectionAreaModel.getModel())).toList()
        );
    }
}
