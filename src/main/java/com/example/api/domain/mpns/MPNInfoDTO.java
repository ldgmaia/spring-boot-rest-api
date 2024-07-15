package com.example.api.domain.mpns;

public record MPNInfoDTO(
        Long id,
        String name,
        String description,
        String status,
        Boolean enabled
) {
    public MPNInfoDTO(MPN mpn) {
        this(
                mpn.getId(),
                mpn.getName(),
                mpn.getDescription(),
                mpn.getStatus(),
                mpn.getEnabled()
        );
    }
}
