package com.example.api.domain.locations;

public record LocationInfoDTO(
        Long id,
        String name
//        UserInfoDTO createdBy
) {
    public LocationInfoDTO(Location location) {
        this(
                location.getId(),
                location.getName()
//                new UserInfoDTO(location.getCreatedBy())
        );
    }
}
