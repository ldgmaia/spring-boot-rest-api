package com.example.api.domain.sections;

import com.example.api.domain.sectionareas.SectionAreaInfoDTO;

import java.util.List;

public record SectionWithAreasDTO(
        Long id,
        String name,
        Long sectionOrder,
        List<SectionAreaInfoDTO> areas
) {
    public SectionWithAreasDTO(Section section, List<SectionAreaInfoDTO> areas) {
        this(
                section.getId(),
                section.getName(),
                section.getSectionOrder(),
                areas
        );
    }
}
