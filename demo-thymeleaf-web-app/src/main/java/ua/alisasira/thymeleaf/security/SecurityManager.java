package ua.alisasira.thymeleaf.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityManager {

    public static void authenticate(AuthenticationManager authenticationManager, UsernamePasswordAuthenticationToken authentication) {
        SecurityContextHolder.getContext().setAuthentication(authenticationManager.authenticate(authentication));
    }

    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static boolean hasRole(String role) {
        Authentication authentication = getAuthentication();

        if (!authentication.isAuthenticated()) {
            return false;
        }

        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if (authority.getAuthority().equalsIgnoreCase(role)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAuthenticated() {
        return getAuthentication().isAuthenticated();
    }

    public static Long getCurrentUserId() {
        Authentication authentication = getAuthentication();

        if (!authentication.isAuthenticated()) {
            return -1L;
        }
        return ((ExtendedUserDetails) authentication.getPrincipal()).getId();
    }
}