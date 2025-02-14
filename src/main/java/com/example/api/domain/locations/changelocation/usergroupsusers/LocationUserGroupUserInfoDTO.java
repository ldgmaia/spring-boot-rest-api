package com.example.api.domain.locations.changelocation.usergroupsusers;


public record LocationUserGroupUserInfoDTO(
        Long userId,
        String userName
) {
    public LocationUserGroupUserInfoDTO(LocationUserGroupUser entity) {
        this(
                entity.getUser().getId(),
                entity.getUser().getUsername()
        );
    }
}
