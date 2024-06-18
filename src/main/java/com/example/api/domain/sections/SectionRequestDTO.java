package com.example.api.domain.sections;

import com.example.api.domain.sectionareas.SectionAreaRequestDTO;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record SectionRequestDTO(
        @NotBlank
        String name,

        Long sectionOrder,

//        @NotNull
//        Long modelId,

        List<SectionAreaRequestDTO> areas
) {
}
