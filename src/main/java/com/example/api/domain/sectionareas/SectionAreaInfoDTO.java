package com.example.api.domain.sectionareas;

import java.util.List;

public record SectionAreaInfoDTO(
        Long id,
        String name,
        Long areaOrder,
        Boolean printOnLabel,
        Boolean printAreaNameOnLabel,
        Long orderOnLabel,
        Boolean isCritical,
        List<Long> models
) {
    public SectionAreaInfoDTO(SectionArea sectionArea) {
        this(
                sectionArea.getId(),
                sectionArea.getName(),
                sectionArea.getAreaOrder(),
                sectionArea.getPrintOnLabel(),
                sectionArea.getPrintAreaNameOnLabel(),
                sectionArea.getOrderOnLabel(),
                sectionArea.getIsCritical(),
                List.of()
        );
    }

    public SectionAreaInfoDTO(SectionAreaInfoDTO sectionArea, List<Long> models) {
        this(
                sectionArea.id(),
                sectionArea.name(),
                sectionArea.areaOrder(),
                sectionArea.printOnLabel(),
                sectionArea.printAreaNameOnLabel(),
                sectionArea.orderOnLabel(),
                sectionArea.isCritical(),
                models
        );
    }
}
