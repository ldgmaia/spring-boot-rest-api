package com.example.api.domain.sectionareas;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record SectionAreaUpdateDTO(
        Long id,

        @NotBlank
        String name,

        Long areaOrder,
        Boolean printOnLabel,
        Boolean printAreaNameOnLabel,
        Long orderOnLabel,
        Boolean isCritical,
        List<Long> models
) {
}
