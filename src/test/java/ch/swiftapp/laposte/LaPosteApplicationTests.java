package ch.swiftapp.laposte;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

/**
 * Smoke test — verifies the Spring Boot application context loads successfully.
 * Uses Testcontainers for PostgreSQL and Redis.
 */
@SpringBootTest
@Import(TestcontainersConfig.class)
@ActiveProfiles("test")
class LaPosteApplicationTests {

    @Test
    void contextLoads() {
        // Context loads successfully = all beans wired correctly
    }
}

