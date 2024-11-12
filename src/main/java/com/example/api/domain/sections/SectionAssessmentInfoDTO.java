package com.example.api.domain.sections;

import com.example.api.domain.sectionareas.SectionAreaAssessmentInfoDTO;

import java.util.List;

public record SectionAssessmentInfoDTO(
        Long id,
        String name,
        Long sectionOrder,
        List<SectionAreaAssessmentInfoDTO> areas
) {
    public SectionAssessmentInfoDTO(Section section) {
        this(
                section.getId(),
                section.getName(),
                section.getSectionOrder(),
                section.getAreas().stream().map(SectionAreaAssessmentInfoDTO::new).toList()
        );
    }
}
