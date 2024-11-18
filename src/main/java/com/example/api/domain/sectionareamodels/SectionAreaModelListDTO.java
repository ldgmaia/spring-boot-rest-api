package com.example.api.domain.sectionareamodels;

public record SectionAreaModelListDTO(
        Long id,
        Long modelId,
        String modelName
) {
    public SectionAreaModelListDTO(SectionAreaModel sectionAreaModel) {
        this(
                sectionAreaModel.getId(),
                sectionAreaModel.getModel().getId(),
                sectionAreaModel.getModel().getName()
        );
    }
}
