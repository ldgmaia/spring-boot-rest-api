package com.example.api.domain.locations.changelocation.usergroupsusers;//package com.example.api.domain.grouptransferpermissions;

import com.example.api.domain.locations.changelocation.usergroups.LocationUserGroupInfoDTO;
import com.example.api.domain.users.UserInfoDTO;

import java.time.LocalDateTime;

public record LocationUserGroupUserInfoDTO(
        Long id,
        LocationUserGroupInfoDTO locationUserGroupInfo,
        UserInfoDTO user,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public LocationUserGroupUserInfoDTO(LocationUserGroupUser save) {
        this(
                save.getId(),
                new LocationUserGroupInfoDTO(save.getGroup()),
                new UserInfoDTO(save.getUser()),
                save.getCreatedAt(),
                save.getUpdatedAt()
        );
    }
}
