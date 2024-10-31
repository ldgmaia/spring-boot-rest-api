package com.example.api.domain.itemstatus;

public record ItemStatusInfoDTO(
        Long id,
        String name
//        UserInfoDTO createdBy
) {
    public ItemStatusInfoDTO(ItemStatus itemStatus) {
        this(
                itemStatus.getId(),
                itemStatus.getName()
//                new UserInfoDTO(itemStatus.getCreatedBy())
        );
    }
}
