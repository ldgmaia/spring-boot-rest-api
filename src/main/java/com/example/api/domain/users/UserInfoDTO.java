package com.example.api.domain.users;

public record UserInfoDTO(
        Long id,
        String username,
        String firstName,
        String lastName,
        String fullName,
        Long storageLevelId
) {
    public UserInfoDTO(User user) {
        this(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getFirstName() + " " + user.getLastName(),
                user.getStorageLevel() != null ? user.getStorageLevel().getId() : null
        );
    }
}
