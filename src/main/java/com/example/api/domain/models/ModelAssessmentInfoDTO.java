package com.example.api.domain.models;

import com.example.api.domain.categoryfields.CategoryFieldsAssessmentInfoDTO;

import java.util.List;

public record ModelAssessmentInfoDTO(
        Long id,
        String name,
        List<CategoryFieldsAssessmentInfoDTO> categoryFields
) {
    public ModelAssessmentInfoDTO(Model model) {
        this(
                model.getId(),
                model.getName(),
                model.getCategory().getCategoryFields().stream().map(CategoryFieldsAssessmentInfoDTO::new).toList()
        );
    }
}
