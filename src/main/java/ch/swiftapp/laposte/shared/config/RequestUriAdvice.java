package ch.swiftapp.laposte.shared.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Makes the current request URI available as {@code requestURI} in all Thymeleaf templates.
 * <p>
 * In Thymeleaf 3.1+ (Spring Boot 3.x), {@code #httpServletRequest} is no longer
 * exposed by default. This advice bridges the gap so layout templates can highlight
 * the active sidebar link.
 */
@ControllerAdvice
public class RequestUriAdvice {

    @ModelAttribute("requestURI")
    public String requestURI(HttpServletRequest request) {
        return request.getRequestURI();
    }
}

