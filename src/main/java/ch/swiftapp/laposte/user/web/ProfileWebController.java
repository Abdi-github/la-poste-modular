package ch.swiftapp.laposte.user.web;

import ch.swiftapp.laposte.user.UserModuleApi;
import ch.swiftapp.laposte.user.dto.response.EmployeeResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * User profile page — displays the authenticated user's information
 * and allows updating language preference.
 */
@Controller
@RequiredArgsConstructor
public class ProfileWebController {

    private final UserModuleApi userModuleApi;

    @GetMapping("/profile")
    public String profile(Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        String email = null;
        String displayName = authentication.getName();
        String keycloakId = null;

        if (authentication.getPrincipal() instanceof OidcUser oidcUser) {
            email = oidcUser.getEmail();
            displayName = oidcUser.getFullName() != null ? oidcUser.getFullName() : oidcUser.getPreferredUsername();
            keycloakId = oidcUser.getSubject();
        }

        // Collect roles
        String roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(a -> a.startsWith("ROLE_"))
                .map(a -> a.replace("ROLE_", ""))
                .collect(Collectors.joining(", "));

        model.addAttribute("displayName", displayName);
        model.addAttribute("email", email);
        model.addAttribute("roles", roles);
        model.addAttribute("keycloakId", keycloakId);

        // Try to find employee profile
        if (email != null) {
            Optional<EmployeeResponse> empOpt = userModuleApi.findEmployeeByEmail(email);
            empOpt.ifPresent(emp -> {
                model.addAttribute("employee", emp);
                model.addAttribute("branchId", emp.getAssignedBranchId());
            });
        }

        return "profile/view";
    }

    @PostMapping("/profile/language")
    public String changeLanguage(@RequestParam String lang, HttpServletResponse response, RedirectAttributes ra) {
        Cookie cookie = new Cookie("lang", lang);
        cookie.setPath("/");
        cookie.setMaxAge(365 * 24 * 60 * 60); // 1 year
        cookie.setHttpOnly(false);
        response.addCookie(cookie);
        ra.addFlashAttribute("successMessage", "Language updated");
        return "redirect:/profile?lang=" + lang;
    }
}

