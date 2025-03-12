package com.example.api.domain.locations.changelocation.usergroups;

import com.example.api.domain.users.UserInfoDTO;

import java.util.List;

public record LocationUserGroupInfoDTO(
        Long id,
        String name,
        String description,
        List<UserInfoDTO> users
) {
    public LocationUserGroupInfoDTO(LocationUserGroup data) {
        this(
                data.getId(),
                data.getName(),
                data.getDescription(),
                data.getUsers().stream().map(user -> new UserInfoDTO(user.getUser())).toList()
        );
    }
}
