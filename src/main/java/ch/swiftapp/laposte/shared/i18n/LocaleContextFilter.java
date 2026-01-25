package ch.swiftapp.laposte.shared.i18n;

import ch.swiftapp.laposte.shared.constants.SupportedLocales;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Locale;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
public class LocaleContextFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (request instanceof HttpServletRequest httpRequest) {
            String acceptLanguage = httpRequest.getHeader("Accept-Language");
            String resolved = SupportedLocales.normalize(acceptLanguage);
            LocaleContextHolder.setLocale(Locale.forLanguageTag(resolved));
        }
        try {
            chain.doFilter(request, response);
        } finally {
            LocaleContextHolder.resetLocaleContext();
        }
    }
}

