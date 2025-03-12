package com.example.api.domain.users;

public record UserInfoDTO(Long id, String username, String firstName, String lastName, String fullName) {

    public UserInfoDTO(User user) {
        this(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getFirstName() + " " + user.getLastName()
        );
    }
}
