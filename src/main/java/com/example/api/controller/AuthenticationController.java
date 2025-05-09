package com.example.api.controller;

import com.example.api.domain.permission.Role;
import com.example.api.domain.users.AuthenticationDTO;
import com.example.api.domain.users.User;
import com.example.api.infra.security.JWTTokenDTO;
import com.example.api.infra.security.TokenService;
import com.example.api.repositories.UserPermissionRepository;
import com.example.api.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("login")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserPermissionRepository userPermissionRepository;

    @PostMapping
    public ResponseEntity<?> handleLogin(@RequestBody @Valid AuthenticationDTO data) {
        try {
            var authenticationToken = new UsernamePasswordAuthenticationToken(data.username(), data.password());
            Authentication authentication = manager.authenticate(authenticationToken);

            User user = (User) authentication.getPrincipal();
            var tokenJWT = tokenService.generateToken(user);

            List<Role> roles = userPermissionRepository.findRolesByUserId(user.getId());
            // List<Role> roles = userPermissionRepository.findRolesByUserId(user.getId()).stream().filter(role -> role.getName().equals("ADMIN")).toList();
            // List<String> roleNames = roles.stream().map(Role::getName).collect(Collectors.toList());
            List<String> roleNames = new ArrayList<>();
            roleNames.add("ADMIN");

            if (user.getStorageLevel() == null) {
                return ResponseEntity.ok(new JWTTokenDTO(user.getId(), user.getUsername(), user.getFirstName(), user.getLastName(), null, roleNames, tokenJWT));
            }
            return ResponseEntity.ok(new JWTTokenDTO(user.getId(), user.getUsername(), user.getFirstName(), user.getLastName(), user.getStorageLevel().getId(), roleNames, tokenJWT));
        } catch (BadCredentialsException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Invalid username or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }
}
