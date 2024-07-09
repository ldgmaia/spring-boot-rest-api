package com.example.api.domain.mpns;

public record MPNInfoDTO(
        Long id,
        String name,
        String description,
        String status,
        Boolean enabled

) {
}
