package ch.swiftapp.laposte.shared.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 * Exception handler for Thymeleaf web controllers (non-API).
 * Returns error page templates instead of JSON responses.
 */
@Slf4j
@ControllerAdvice(basePackages = {
        "ch.swiftapp.laposte.branch.web",
        "ch.swiftapp.laposte.delivery.web",
        "ch.swiftapp.laposte.notification.web",
        "ch.swiftapp.laposte.parcel.web",
        "ch.swiftapp.laposte.tracking.web",
        "ch.swiftapp.laposte.user.web",
        "ch.swiftapp.laposte.address.web",
        "ch.swiftapp.laposte.dashboard.web",
        "ch.swiftapp.laposte.shared.web"
})
public class WebExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ModelAndView handleNotFound(ResourceNotFoundException ex) {
        log.warn("Web 404: {}", ex.getMessage());
        ModelAndView mav = new ModelAndView("error/404");
        mav.setStatus(HttpStatus.NOT_FOUND);
        mav.addObject("errorMessage", ex.getMessage());
        return mav;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView handleAccessDenied(AccessDeniedException ex) {
        log.warn("Web 403: {}", ex.getMessage());
        ModelAndView mav = new ModelAndView("error/403");
        mav.setStatus(HttpStatus.FORBIDDEN);
        return mav;
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ModelAndView handleBusinessRule(BusinessRuleException ex) {
        log.warn("Web business rule: {}", ex.getMessage());
        ModelAndView mav = new ModelAndView("error/500");
        mav.setStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        mav.addObject("errorMessage", ex.getMessage());
        return mav;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleGeneric(Exception ex) {
        log.error("Web 500: {}", ex.getMessage(), ex);
        ModelAndView mav = new ModelAndView("error/500");
        mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        mav.addObject("errorMessage", "An unexpected error occurred. Please try again.");
        return mav;
    }
}

