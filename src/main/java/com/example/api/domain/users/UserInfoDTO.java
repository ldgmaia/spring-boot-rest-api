package com.example.api.domain.users;

public record UserInfoDTO(Long id, String username, String firstName, String lastName) {

    public UserInfoDTO(User user) {
        this(user.getId(), user.getUsername(), user.getFirstName(), user.getLastName());
    }

}
