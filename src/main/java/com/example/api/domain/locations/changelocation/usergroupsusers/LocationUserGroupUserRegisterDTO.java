package com.example.api.domain.locations.changelocation.usergroupsusers;

import com.example.api.domain.locations.changelocation.usergroups.LocationUserGroup;
import com.example.api.domain.users.User;
import jakarta.validation.constraints.NotNull;

public record LocationUserGroupUserRegisterDTO(
        @NotNull
        LocationUserGroup locationUserGroup,

        @NotNull
        User user
) {
}
