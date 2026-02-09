package ch.swiftapp.laposte.notification.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

/**
 * Email sending service backed by JavaMailSender.
 * Controlled via {@code app.mail.enabled} flag — when false, emails are only logged.
 * In dev, Mailpit catches all outbound mail on port 1028.
 */
@Slf4j
@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final boolean mailEnabled;
    private final String fromAddress;
    private final String fromName;

    public EmailService(JavaMailSender mailSender,
                        @Value("${app.mail.enabled:false}") boolean mailEnabled,
                        @Value("${app.mail.from:noreply@laposte.ch}") String fromAddress,
                        @Value("${app.mail.from-name:La Poste Suisse}") String fromName) {
        this.mailSender = mailSender;
        this.mailEnabled = mailEnabled;
        this.fromAddress = fromAddress;
        this.fromName = fromName;
    }

    /**
     * Send an email. If {@code app.mail.enabled} is false, just logs.
     *
     * @param to      recipient email
     * @param subject email subject
     * @param body    email body (plain text)
     * @throws MessagingException if sending fails
     */
    public void send(String to, String subject, String body) throws MessagingException, UnsupportedEncodingException {
        send(to, subject, body, false);
    }

    /**
     * Send an email with optional HTML support.
     */
    public void send(String to, String subject, String body, boolean html) throws MessagingException, UnsupportedEncodingException {
        if (!mailEnabled) {
            log.info("[MAIL DISABLED] Would send to={}, subject='{}'", to, subject);
            return;
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
        helper.setFrom(fromAddress, fromName);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, html);

        mailSender.send(message);
        log.info("Email sent to={}, subject='{}', html={}", to, subject, html);
    }
}





