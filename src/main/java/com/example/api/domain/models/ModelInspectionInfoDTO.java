package com.example.api.domain.models;

import com.example.api.domain.categoryfields.CategoryFieldsAssessmentInfoDTO;
import com.example.api.domain.mpns.MPNInfoDTO;

import java.util.List;

public record ModelInspectionInfoDTO(
        Long id,
        String name,
        Boolean needsSerialNumber,
        Boolean needsMpn,
        Boolean isParent,
        Long currentMpnId,
        List<MPNInfoDTO> mpns,
        List<CategoryFieldsAssessmentInfoDTO> categoryFields
) {
    public ModelInspectionInfoDTO(Model model, Long currentMpnId) {

        this(
                model.getId(),
                model.getName(),
                model.getCategory().getNeedsSerialNumber(),
                model.getNeedsMpn(),
                !model.getCategory().getParentCategory().isEmpty(),
                model.getNeedsMpn() ? currentMpnId : null,
                model.getMpns().stream().map(MPNInfoDTO::new).toList(),
                model.getCategory().getCategoryFields().stream().map(CategoryFieldsAssessmentInfoDTO::new).toList()
        );
    }
}
