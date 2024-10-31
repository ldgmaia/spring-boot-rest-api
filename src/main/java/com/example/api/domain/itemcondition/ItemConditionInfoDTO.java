package com.example.api.domain.itemcondition;

public record ItemConditionInfoDTO(
        Long id,
        String name
//        UserInfoDTO createdBy
) {
    public ItemConditionInfoDTO(ItemCondition itemCondition) {
        this(
                itemCondition.getId(),
                itemCondition.getName()
//                new UserInfoDTO(itemCondition.getCreatedBy())
        );
    }
}
