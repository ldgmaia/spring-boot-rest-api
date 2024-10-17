package com.example.api.domain.locations;

import com.example.api.domain.users.User;

public record LocationInfoDTO(
        Long id,
        String name,
        User createdBy
) {
    public LocationInfoDTO(Location location) {
        this(
                location.getId(),
                location.getName(),
                location.getCreatedBy()
        );
    }
}
