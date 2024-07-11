package com.example.api.domain.mpns;

import com.example.api.domain.mpnfieldsvalues.MPNFieldValueInfoDTO;

import java.util.List;

public record MPNInfoDTO(
        Long id,
        String name,
        String description,
        String status,
        Boolean enabled,
        List<MPNFieldValueInfoDTO> mpnFieldsValues
) {
    public MPNInfoDTO(MPN mpn, List<MPNFieldValueInfoDTO> mpnFieldsValues) {
        this(
                mpn.getId(),
                mpn.getName(),
                mpn.getDescription(),
                mpn.getStatus(),
                mpn.getEnabled(),
                mpnFieldsValues
        );
    }

    public MPNInfoDTO(MPN mpn) {
        this(
                mpn.getId(),
                mpn.getName(),
                mpn.getDescription(),
                mpn.getStatus(),
                mpn.getEnabled(),
                null
        );
    }
}
