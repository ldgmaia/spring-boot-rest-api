package com.example.api.domain.sections;

public record SectionInfoDTO(
        Long id,
        String name,
        Long sectionOrder
) {
    public SectionInfoDTO(Section section) {
        this(
                section.getId(),
                section.getName(),
                section.getSectionOrder()
        );
    }
}


