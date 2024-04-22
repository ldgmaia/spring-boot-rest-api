package com.example.api.controller;

import com.example.api.domain.users.AuthenticationDTO;
import com.example.api.domain.users.User;
import com.example.api.infra.security.JWTTokenDTO;
import com.example.api.infra.security.TokenService;
import com.example.api.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity handleLogin(@RequestBody @Valid AuthenticationDTO data) {

        var authenticationToken = new UsernamePasswordAuthenticationToken(data.username(), data.password());
        var authentication = manager.authenticate(authenticationToken);

        System.out.println(authentication);

        var tokenJWT = tokenService.generateToken((User) authentication.getPrincipal());

        var user = userRepository.findByUsername(data.username());

        return ResponseEntity.ok(new JWTTokenDTO(user.getId(), user.getUsername(), user.getFirstName(), user.getLastName(), tokenJWT));
    }
}
