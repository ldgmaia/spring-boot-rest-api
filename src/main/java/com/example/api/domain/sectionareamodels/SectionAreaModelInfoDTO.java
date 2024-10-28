package com.example.api.domain.sectionareamodels;

public record SectionAreaModelInfoDTO(Long id) {
    // Constructor to create DTO with just the model ID
    public SectionAreaModelInfoDTO(Long id) {
        this.id = id;
    }
}
