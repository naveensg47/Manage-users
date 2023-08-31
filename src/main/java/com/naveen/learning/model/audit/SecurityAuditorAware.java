package com.naveen.learning.model.audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class SecurityAuditorAware implements AuditorAware<String> {

    public static final String SYSTEM="System";

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        return Optional.ofNullable(authentication.getPrincipal().toString());
    }
}
