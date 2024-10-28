package com.example.api.domain.sectionareamodels;

public record SectionAreaModeIdListDTO(
        Long id,
        Long modelId,
        String modelName
) {
    public SectionAreaModeIdListDTO(SectionAreaModel sectionAreaModel) {
        this(
                sectionAreaModel.getId(),
                sectionAreaModel.getModel().getId(),
                sectionAreaModel.getModel().getName()
        );
    }
}
