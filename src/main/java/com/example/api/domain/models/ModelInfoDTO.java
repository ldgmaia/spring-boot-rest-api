package com.example.api.domain.models;

import com.example.api.domain.categories.CategoryInfoDTO;

public record ModelInfoDTO(
        Long id,
        String name,
        String description,
        String identifier,
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
                model.getEnabled(),
                model.getNeedsMpn(),
                new CategoryInfoDTO(model.getCategory())
//                field.getFieldGroup() != null ? new FieldGroupInfoDTO(field.getFieldGroup()) : null); // another way of doing the same check
//                Optional.ofNullable(field.getFieldGroup()).map(FieldGroupInfoDTO::new).orElse(null));
        );
    }
}


