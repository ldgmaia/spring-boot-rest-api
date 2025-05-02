package com.example.api.domain.locations.changelocation.usergroupsusers;


public record LocationUserGroupUserInfoDTO(
        Long userId,
        String userName,
        String fullName
) {
    public LocationUserGroupUserInfoDTO(LocationUserGroupUser data) {
        this(
                data.getUser().getId(),
                data.getUser().getUsername(),
                data.getUser().getFullName()
        );
    }
}
