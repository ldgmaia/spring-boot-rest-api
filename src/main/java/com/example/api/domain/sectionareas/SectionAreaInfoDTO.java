package com.example.api.domain.sectionareas;

public record SectionAreaInfoDTO(
        Long id,
        String name,
        Long areaOrder,
        Boolean printOnLabel,
        Boolean printAreaNameOnLabel,
        Long orderOnLabel,
        Boolean isCritical
) {
    public SectionAreaInfoDTO(SectionArea sectionArea) {
        this(
                sectionArea.getId(),
                sectionArea.getName(),
                sectionArea.getAreaOrder(),
                sectionArea.getPrintOnLabel(),
                sectionArea.getPrintAreaNameOnLabel(),
                sectionArea.getOrderOnLabel(),
                sectionArea.getIsCritical()
        );
    }
}


