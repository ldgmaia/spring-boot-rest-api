package com.example.api.domain.itemstatus;

import com.example.api.domain.users.UserInfoDTO;

public record ItemStatusInfoDTO(
        Long id,
        String name,
        UserInfoDTO createdBy
) {
    public ItemStatusInfoDTO(ItemStatus itemStatus) {
        this(
                itemStatus.getId(),
                itemStatus.getName(),
                new UserInfoDTO(itemStatus.getCreatedBy())
        );
    }
}
