package com.example.api.domain.locations.changelocation.usergroupspermission;//package com.example.api.domain.grouptransferpermissions;

import com.example.api.domain.locations.changelocation.usergroups.LocationUserGroup;
import com.example.api.domain.storage.storagearea.StorageArea;
import jakarta.validation.constraints.NotNull;

public record LocationUserGroupPermissionRegisterDTO(
        @NotNull
        LocationUserGroup locationUserGroup,

        @NotNull
        StorageArea fromLocationArea,

        @NotNull
        StorageArea toLocationArea
) {
}
