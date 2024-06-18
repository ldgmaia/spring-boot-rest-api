package com.example.api.domain.sectionareas;

import com.example.api.domain.sections.Section;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SectionAreaRegisterDTO(
        @NotBlank
        String name,

        @NotNull
        Section section,

        Long areaOrder,
        Boolean printOnLabel,
        Boolean printAreaNameOnLabel,
        Long orderOnLabel,
        Boolean isCritical
) {
}
