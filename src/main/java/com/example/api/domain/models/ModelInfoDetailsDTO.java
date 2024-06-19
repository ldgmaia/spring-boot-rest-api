package com.example.api.domain.models;

import com.example.api.domain.categories.CategoryInfoDTO;
import com.example.api.domain.modelfieldsvalues.ModelFieldValueInfoDTO;
import com.example.api.domain.sections.SectionWithAreasDTO;

import java.util.List;

public record ModelInfoDetailsDTO(
        Long id,
        String name,
        String description,
        String identifier,
        Boolean enabled,
        Boolean needsMpn,
        CategoryInfoDTO category,
        List<ModelFieldValueInfoDTO> modelFieldsValues,
        List<SectionWithAreasDTO> sections
) {
}


