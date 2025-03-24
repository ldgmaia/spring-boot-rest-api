package com.example.api.controller;

import com.example.api.domain.users.UserInfoDTO;
import com.example.api.domain.users.UserRegisterDTO;
import com.example.api.domain.users.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("users")
@SecurityRequirement(name = "bearer-key")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    @Transactional
    @Modifying
    public ResponseEntity register(@RequestBody @Valid UserRegisterDTO data, UriComponentsBuilder uriBuilder) {
        var newUser = userService.register(data);
        var uri = uriBuilder.path("/users/{id}").buildAndExpand(newUser.getId()).toUri();

        return ResponseEntity.created(uri).body(new UserInfoDTO(newUser));
    }

//    @PutMapping("/deactivate/{userId}")
//    public ResponseEntity<Void> deactivateUser(@PathVariable Long userId) {
//        userService.deactivateUser(userId);
//        return ResponseEntity.noContent().build();
//    }

    @GetMapping
    public ResponseEntity list() {
        return ResponseEntity.ok(userService.list());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity getById(@Valid @PathVariable Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }
}
