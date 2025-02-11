package com.example.api.domain.locations.changelocation.usergroups;//package com.example.api.domain.grouptransferpermissions;

import jakarta.validation.constraints.NotBlank;

public record LocationUserGroupRequestDTO(
        @NotBlank
        String name,

        String description
) {
}
