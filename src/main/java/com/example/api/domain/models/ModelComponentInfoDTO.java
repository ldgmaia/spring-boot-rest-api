package com.example.api.domain.models;

public record ModelComponentInfoDTO(
        Long id,
        String name
) {
    public ModelComponentInfoDTO(Model model) {
        this(
                model.getId(),
                model.getName()
        );
    }
}
