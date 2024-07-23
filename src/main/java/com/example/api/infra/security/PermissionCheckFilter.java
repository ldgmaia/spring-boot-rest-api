package com.example.api.infra.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.api.domain.users.User;
import com.example.api.repositories.UserPermissionRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class PermissionCheckFilter extends OncePerRequestFilter {

    @Autowired
    private UserPermissionRepository userPermissionRepository;

    @Autowired
    private TokenService tokenService;

    @Value("${api.security.token.secret}")
    private String secret;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String method = request.getMethod();
//        String uri = request.getRequestURI();
        String uri = request.getRequestURI().replaceAll("/\\d+", "/id"); // if the route has parameter, such as "DELETE /api/v1/doctors/1", replaces the "1" for the word "id"


        // Checking if the user is admin, so there is no need to check any permission
        var tokenJWT = getToken(request);

        if (tokenJWT != null) {
            try {
                var roles = tokenService.getRoles(tokenJWT);

                if (roles.contains("admin")) {
                    // Skip any further filter because this user is Admin and should have access to everything
                    filterChain.doFilter(request, response);
                    return;
                }

            } catch (JWTVerificationException exception) {
                throw new RuntimeException("Token JWT invalid or expired");
            }
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof User userDetails) {

            if (!userPermissionRepository.existsByUserIdAndPermission(userDetails.getId(), method + " " + uri)) { // the value is "GET /api/v1/doctors"
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("User has no permission to perform this action");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null) {
            return authorizationHeader.replace("Bearer ", "");
        }

        return null;
    }
}
