package ch.swiftapp.laposte.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

/**
 * Renders Thymeleaf email templates into HTML strings.
 * Templates live under resources/templates/email/*.html.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailTemplateService {

    private final TemplateEngine templateEngine;

    /**
     * Render an email template to HTML.
     *
     * @param templateName template name (e.g., "parcel-created") — resolved to email/parcel-created.html
     * @param variables    template variables
     * @return rendered HTML string
     */
    public String render(String templateName, Map<String, Object> variables) {
        Context ctx = new Context();
        ctx.setVariables(variables);
        String html = templateEngine.process("email/" + templateName, ctx);
        log.debug("Rendered email template: {}", templateName);
        return html;
    }
}

