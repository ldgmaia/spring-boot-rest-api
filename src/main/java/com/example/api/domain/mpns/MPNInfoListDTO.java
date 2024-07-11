package com.example.api.domain.mpns;

import com.example.api.domain.mpnfieldsvalues.MPNFieldValueInfoDTO;

import java.util.List;

public record MPNInfoListDTO(
        Long id,
        String name,
        String modelName,
        String status,
        Boolean enabled,
        String createdBy,
        String approvedBy,
        List<MPNFieldValueInfoDTO> mpnFieldsValues // Add this field
) {
    public MPNInfoListDTO(
            Long id,
            String name,
            String modelName,
            String status,
            Boolean enabled,
            String createdBy,
            String approvedBy) {
        this(id, name, modelName, status, enabled, createdBy, approvedBy, null);
    }
}
