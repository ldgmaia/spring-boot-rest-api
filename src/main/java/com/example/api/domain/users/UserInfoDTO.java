package com.example.api.domain.users;

public record UserInfoDTO(Long id, String username) {

    public UserInfoDTO(User user) {
        this(user.getId(), user.getUsername());
    }

}
