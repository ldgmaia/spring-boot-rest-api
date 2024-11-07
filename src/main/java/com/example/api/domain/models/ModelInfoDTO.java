package com.example.api.domain.models;

import com.example.api.domain.categories.CategoryInfoDTO;

public record ModelInfoDTO(
        Long id,
        String name,
        String description,
        String identifier,
        String status,
        Boolean enabled,
        Boolean needsMpn,
        CategoryInfoDTO category
) {
    public ModelInfoDTO(Model model) {
        this(
                model.getId(),
                model.getName(),
                model.getDescription(),
                model.getIdentifier(),
                model.getStatus(),
                model.getEnabled(),
                model.getNeedsMpn(),
                new CategoryInfoDTO(model.getCategory())
        );
    }
}
