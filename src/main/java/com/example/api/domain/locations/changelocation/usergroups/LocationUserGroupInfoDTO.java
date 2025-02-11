package com.example.api.domain.locations.changelocation.usergroups;

import com.example.api.domain.locations.changelocation.usergroupsusers.LocationUserGroupUser;

import java.time.LocalDateTime;

public record LocationUserGroupInfoDTO(
        Long id,
        String name,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public LocationUserGroupInfoDTO(LocationUserGroup save) {
        this(
                save.getId(),
                save.getName(),
                save.getDescription(),
                save.getCreatedAt(),
                save.getUpdatedAt()
        );
    }

    public LocationUserGroupInfoDTO(LocationUserGroupUser locationUserGroupUser) {
        this(
                locationUserGroupUser.getGroup().getId(),
                locationUserGroupUser.getGroup().getName(),
                locationUserGroupUser.getGroup().getDescription(),
                locationUserGroupUser.getGroup().getCreatedAt(),
                locationUserGroupUser.getGroup().getUpdatedAt()
        );
    }
}
