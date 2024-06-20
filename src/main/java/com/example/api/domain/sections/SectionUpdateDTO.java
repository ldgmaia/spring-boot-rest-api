package com.example.api.domain.sections;

import com.example.api.domain.sectionareas.SectionAreaUpdateDTO;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record SectionUpdateDTO(
        Long id,

        @NotBlank
        String name,

        Long sectionOrder,

//        @NotNull
//        Long modelId,

        List<SectionAreaUpdateDTO> areas
) {
}
