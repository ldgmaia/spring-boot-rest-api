package com.example.api.domain.locations.changelocation.usergroups;

public record LocationUserGroupInfoDTO(
        Long id,
        String name,
        String description
) {
    public LocationUserGroupInfoDTO(LocationUserGroup savedGroup) {
        this(
                savedGroup.getId(),
                savedGroup.getName(),
                savedGroup.getDescription()
        );
    }
}
