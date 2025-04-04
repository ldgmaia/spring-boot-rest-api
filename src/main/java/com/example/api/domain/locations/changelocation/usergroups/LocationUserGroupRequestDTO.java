package com.example.api.domain.locations.changelocation.usergroups;//package com.example.api.domain.grouptransferpermissions;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record LocationUserGroupRequestDTO(
        @NotBlank
        String name,

        String description,

        @NotNull
        List<Long> users
) {
}
