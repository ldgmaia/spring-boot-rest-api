package com.example.api.domain.sectionareas;

import jakarta.validation.constraints.NotBlank;

public record SectionAreaUpdateDTO(
        Long id,

        @NotBlank
        String name,

//        @NotNull
//        Long sectionId,

        Long areaOrder,
        Boolean printOnLabel,
        Boolean printAreaNameOnLabel,
        Long orderOnLabel,
        Boolean isCritical
) {
}
