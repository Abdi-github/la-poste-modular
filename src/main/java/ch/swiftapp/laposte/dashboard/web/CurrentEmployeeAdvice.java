package ch.swiftapp.laposte.dashboard.web;

import ch.swiftapp.laposte.user.UserModuleApi;
import ch.swiftapp.laposte.user.dto.response.EmployeeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Resolves the current authenticated user's employee ID and makes it available
 * as {@code currentEmployeeId} in all Thymeleaf templates.
 * Used by the notification bell in the layout header.
 */
@ControllerAdvice
@RequiredArgsConstructor
public class CurrentEmployeeAdvice {

    private final UserModuleApi userModuleApi;

    @ModelAttribute("currentEmployeeId")
    public Long currentEmployeeId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return null;

        String email = null;

        // Try OIDC user (web dashboard login)
        if (auth.getPrincipal() instanceof OidcUser oidcUser) {
            email = oidcUser.getEmail();
            if (email == null) email = oidcUser.getPreferredUsername();
        }

        if (email == null) return null;

        // Find employee by email
        try {
            return userModuleApi.findEmployeeByEmail(email)
                    .map(EmployeeResponse::getId)
                    .orElse(null);
        } catch (Exception e) {
            return null;
        }
    }
}


