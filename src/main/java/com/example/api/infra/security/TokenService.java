package com.example.api.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.api.domain.permission.Role;
import com.example.api.domain.users.User;
import com.example.api.repositories.UserPermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    @Autowired
    private UserPermissionRepository userPermissionRepository;

    public String generateToken(User user) {
        try {
            List<Role> roles = userPermissionRepository.findRolesByUserId(user.getId());
            List<String> roleNames = roles.stream().map(Role::getName).collect(Collectors.toList());
            var algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("API RBMS")
                    .withSubject(user.getUsername())
                    .withClaim("roles", roleNames)
                    .withExpiresAt(expirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error generating JWT token", exception);
        }

    }

    public String getSubject(String tokenJWT) {
        try {
            var algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    // specify any specific claim validations
                    .withIssuer("API RBMS")
                    // reusable verifier instance
                    .build()
                    .verify(tokenJWT)
                    .getSubject();

        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Token JWT invalid or expired");
        }
    }

    public List<String> getRoles(String tokenJWT) {
        try {
            var algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    // specify any specific claim validations
                    .withIssuer("API RBMS")
                    // reusable verifier instance
                    .build()
                    .verify(tokenJWT)
                    .getClaim("roles")
                    .asList(String.class);

        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Token JWT invalid or expired");
        }
    }

    public boolean isTokenExpired(String tokenJWT) {
        try {
            var algorithm = Algorithm.HMAC256(secret);
            DecodedJWT decodedJWT = JWT.require(algorithm)
                    .withIssuer("API RBMS")
                    .build()
                    .verify(tokenJWT);
            return decodedJWT.getExpiresAt().before(new Date());
        } catch (JWTVerificationException exception) {
            return true; // If there's a verification exception, consider the token as expired or invalid
        }
    }

    private Instant expirationDate() {
        return LocalDateTime.now().plusHours(10).toInstant(ZoneOffset.of("-05:00"));
    }
}
