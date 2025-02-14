package com.example.api.domain.locations.changelocation.usergroupspermission;//package com.example.api.domain.grouptransferpermissions;

import jakarta.validation.constraints.NotNull;

public record LocationUserGroupPermissionRequestDTO(
        @NotNull
        Long locationUserGroupId,

        @NotNull
        Long fromLocationAreaId,

        @NotNull
        Long toLocationAreaId
) {
}
