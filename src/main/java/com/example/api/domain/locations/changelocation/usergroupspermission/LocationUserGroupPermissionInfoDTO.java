package com.example.api.domain.locations.changelocation.usergroupspermission;//package com.example.api.domain.grouptransferpermissions;

import java.time.LocalDateTime;

public record LocationUserGroupPermissionInfoDTO(
        Long id,
        Long locationUserGroupId,
        Long fromLocationId,
        Long toLocationId,
//        String name,
//        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public LocationUserGroupPermissionInfoDTO(LocationUserGroupPermission save) {
        this(
                save.getId(),
                save.getLocationUserGroup().getId(),
                save.getFromLocationArea().getId(),
                save.getToLocationArea().getId(),
//                save.getName(),
//                save.getDescription(),
                save.getCreatedAt(),
                save.getUpdatedAt()
        );
    }
}
