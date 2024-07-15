package com.example.api.domain.mpns;

import com.example.api.domain.mpnfieldsvalues.MPNFieldValueInfoDTO;

import java.util.List;

public record MPNInfoDetailsDTO(
        Long id,
        String name,
        String description,
        String status,
        Boolean enabled,
        List<MPNFieldValueInfoDTO> mpnFieldsValues
) {
}
