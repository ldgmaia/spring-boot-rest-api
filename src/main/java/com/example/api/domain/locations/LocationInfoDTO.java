package com.example.api.domain.locations;

public record LocationInfoDTO(
        Long id,
        String name
) {
    public LocationInfoDTO(Location location) {
        this(
                location.getId(),
                location.getName()
        );
    }
}
