package com.example.api.infra.security.Auditing;

import com.example.api.domain.users.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class ApplicationAuditAware implements AuditorAware<User> {

    @Override
    public Optional<User> getCurrentAuditor() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("AUTH " + authentication.isAuthenticated());
        if (!authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            System.out.println("I AM HERE");
            return Optional.empty();
        }
        User userPrincipal = (User) authentication.getPrincipal();
        return Optional.ofNullable(userPrincipal);
    }
}
