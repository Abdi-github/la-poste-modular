package ch.swiftapp.laposte.shared.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Security configuration — Keycloak is ALWAYS enabled (dev and prod).
 *
 * <p>Two filter chains:</p>
 * <ol>
 *     <li><b>API chain</b> ({@code /api/**}) — stateless, validates JWT Bearer tokens from Keycloak</li>
 *     <li><b>Web chain</b> ({@code /**}) — session-based, OAuth2 login redirect to Keycloak</li>
 * </ol>
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @org.springframework.beans.factory.annotation.Value("${APP_BASE_URL:http://localhost:8080}")
    private String appBaseUrl;

    // ══════════════════════════════════════════════════════════════
    // Chain 1: REST API — stateless JWT Bearer token validation
    // ══════════════════════════════════════════════════════════════
    @Bean
    @Order(1)
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**")
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**/actuator/**").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/customers").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/tracking/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/addresses/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                );
        return http.build();
    }

    // ══════════════════════════════════════════════════════════════
    // Chain 2: Dashboard — OAuth2 Client login (Keycloak redirect)
    // ══════════════════════════════════════════════════════════════
    @Bean
    @Order(2)
    public SecurityFilterChain webFilterChain(HttpSecurity http,
                                              ClientRegistrationRepository clientRegistrationRepository) throws Exception {
        http
                .securityMatcher("/**")
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/error", "/webjars/**",
                                "/css/**", "/js/**", "/images/**", "/actuator/**",
                                "/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()
                        // ── Role-based page access ──
                        .requestMatchers("/branches/**", "/admin/**", "/addresses/**").hasRole("ADMIN")
                        .requestMatchers("/notifications/compose/**").hasAnyRole("ADMIN", "BRANCH_MANAGER")
                        .requestMatchers("/notifications/inbox/**", "/notifications/bell/**",
                                "/notifications/badge/**", "/notifications/read/**",
                                "/notifications/mark-all-read/**").authenticated()
                        .requestMatchers("/notifications").hasRole("ADMIN")
                        .requestMatchers("/parcels/**", "/employees/**", "/customers/**").hasAnyRole("ADMIN", "BRANCH_MANAGER")
                        .requestMatchers("/pickups/**").hasAnyRole("ADMIN", "BRANCH_MANAGER")
                        .requestMatchers("/deliveries/**", "/tracking/**", "/dashboard/**").authenticated()
                        .requestMatchers("/profile/**").authenticated()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .defaultSuccessUrl("/dashboard", true)
                        .userInfoEndpoint(ui -> ui.userAuthoritiesMapper(keycloakAuthoritiesMapper()))
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler(oidcLogoutSuccessHandler(clientRegistrationRepository))
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                );
        return http.build();
    }

    // ── OIDC Logout — logs out from BOTH the app AND Keycloak ────
    private LogoutSuccessHandler oidcLogoutSuccessHandler(ClientRegistrationRepository repo) {
        OidcClientInitiatedLogoutSuccessHandler handler = new OidcClientInitiatedLogoutSuccessHandler(repo);
        handler.setPostLogoutRedirectUri(appBaseUrl + "/login?logout");
        return handler;
    }

    // ── JWT role extraction from Keycloak realm_access.roles ──────
    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(keycloakRoleConverter());
        return converter;
    }

    private Converter<Jwt, Collection<GrantedAuthority>> keycloakRoleConverter() {
        return jwt -> {
            Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");
            if (realmAccess == null || !realmAccess.containsKey("roles")) {
                return Collections.emptyList();
            }
            @SuppressWarnings("unchecked")
            List<String> roles = (List<String>) realmAccess.get("roles");
            return roles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .collect(Collectors.toList());
        };
    }

    // ── OIDC role extraction for OAuth2 Client login (dashboard) ──
    private GrantedAuthoritiesMapper keycloakAuthoritiesMapper() {
        return authorities -> {
            Set<GrantedAuthority> mapped = new HashSet<>(authorities);
            for (var authority : authorities) {
                if (authority instanceof OidcUserAuthority oidc) {
                    // Try extracting roles from multiple claim sources (Keycloak versions differ)
                    List<String> roles = extractRealmRoles(oidc.getIdToken().getClaims());
                    if (roles.isEmpty() && oidc.getUserInfo() != null) {
                        roles = extractRealmRoles(oidc.getUserInfo().getClaims());
                    }
                    roles.forEach(role -> mapped.add(new SimpleGrantedAuthority("ROLE_" + role)));
                }
            }
            return mapped;
        };
    }

    /**
     * Extract realm roles from a claims map. Handles two Keycloak claim structures:
     * <ul>
     *     <li>Nested: {@code {"realm_access": {"roles": ["ADMIN", ...]}}} (default access token format)</li>
     *     <li>Flat list: {@code {"realm_access.roles": ["ADMIN", ...]}} (when mapped with dot-notation claim name)</li>
     * </ul>
     */
    @SuppressWarnings("unchecked")
    private List<String> extractRealmRoles(Map<String, Object> claims) {
        if (claims == null) return Collections.emptyList();

        // Strategy 1: nested realm_access.roles (standard Keycloak format)
        Object realmAccessObj = claims.get("realm_access");
        if (realmAccessObj instanceof Map<?, ?> realmAccess) {
            Object rolesObj = realmAccess.get("roles");
            if (rolesObj instanceof List<?> roles) {
                return roles.stream()
                        .filter(String.class::isInstance)
                        .map(String.class::cast)
                        .collect(Collectors.toList());
            }
        }

        // Strategy 2: flat "realm_access.roles" claim (dot-notation mapper)
        Object flatRoles = claims.get("realm_access.roles");
        if (flatRoles instanceof List<?> roles) {
            return roles.stream()
                    .filter(String.class::isInstance)
                    .map(String.class::cast)
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }
}

