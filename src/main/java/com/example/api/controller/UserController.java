package com.example.api.controller;

import com.example.api.domain.ValidationException;
import com.example.api.domain.users.User;
import com.example.api.domain.users.UserInfoDTO;
import com.example.api.domain.users.UserRegisterDTO;
import com.example.api.repositories.UserRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("users")
@SecurityRequirement(name = "bearer-key")
public class UserController {

    @Autowired
    private UserRepository repository;

    @PostMapping
    @Transactional
    public ResponseEntity register(@RequestBody @Valid UserRegisterDTO data, UriComponentsBuilder uriBuilder) {

        System.out.println("before");
        if (repository.findByUsername(data.username()) != null) {
            throw new ValidationException("User already registered");
        }
        System.out.println("after");

        var user = new User(data);
        repository.save(user);

        var uri = uriBuilder.path("/users/{id}").buildAndExpand(user.getId()).toUri();

        return ResponseEntity.created(uri).body(new UserInfoDTO(user));
    }
}
