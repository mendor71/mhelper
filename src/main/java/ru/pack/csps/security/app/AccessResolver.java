package ru.pack.csps.security.app;

import org.springframework.security.core.Authentication;

public class AccessResolver {

    public static boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream().anyMatch(v -> v.getAuthority().equals("ROLE_ADMIN") || v.getAuthority().equals("ROLE_USER_ADMIN"));
    }
}
