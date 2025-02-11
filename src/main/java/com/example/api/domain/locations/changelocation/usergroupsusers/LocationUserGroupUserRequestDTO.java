package com.example.api.domain.locations.changelocation.usergroupsusers;//package com.example.api.domain.grouptransferpermissions;

import jakarta.validation.constraints.NotNull;

public record LocationUserGroupUserRequestDTO( // delete this DTO
                                               @NotNull
                                               Long groupId,

                                               Long userId
) {
}
