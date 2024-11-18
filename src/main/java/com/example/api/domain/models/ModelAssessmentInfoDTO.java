package com.example.api.domain.models;

import com.example.api.domain.categoryfields.CategoryFieldsAssessmentInfoDTO;
import com.example.api.domain.mpns.MPNInfoDTO;

import java.util.List;

public record ModelAssessmentInfoDTO(
        Long id,
        String name,
        Boolean needsSerialNumber,
        Boolean needsMpn,
        List<MPNInfoDTO> mpns,
        List<CategoryFieldsAssessmentInfoDTO> categoryFields
) {
    public ModelAssessmentInfoDTO(Model model) {
        this(
                model.getId(),
                model.getName(),
                model.getCategory().getNeedsSerialNumber(),
                model.getNeedsMpn(),
                model.getMpns().stream().map(MPNInfoDTO::new).toList(),
                model.getCategory().getCategoryFields().stream().map(CategoryFieldsAssessmentInfoDTO::new).toList()
        );
    }
}
