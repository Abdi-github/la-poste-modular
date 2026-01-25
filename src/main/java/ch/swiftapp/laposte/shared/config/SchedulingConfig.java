package ch.swiftapp.laposte.shared.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Enables Spring scheduling for background jobs.
 * Controlled by app.scheduler.enabled property (default: false).
 * Set to true in production/dev but disabled during tests.
 */
@Configuration
@EnableScheduling
@ConditionalOnProperty(name = "app.scheduler.enabled", havingValue = "true", matchIfMissing = false)
public class SchedulingConfig {
}

