package com.example.api.domain.locations.changelocation.usergroupspermission;//package com.example.api.domain.grouptransferpermissions;

public record LocationUserGroupPermissionInfoDTO(
        Long id,
        String fromLocationAreaName,
        String toLocationAreaName
) {
    public LocationUserGroupPermissionInfoDTO(LocationUserGroupPermission permission) {
        this(
                permission.getId(),
                permission.getFromLocationArea().getName(),
                permission.getToLocationArea().getName()
        );
    }
}
