package ch.swiftapp.laposte.shared.web;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.health.HealthComponent;
import org.springframework.boot.actuate.health.SystemHealth;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.CompositeHealth;
import org.springframework.boot.info.BuildProperties;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.management.ManagementFactory;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Admin system health/status page — shows DB, Redis, Mail, and Disk Space status.
 */
@Controller
@RequestMapping("/admin/health")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class HealthWebController {

    private final HealthEndpoint healthEndpoint;
    private final Optional<BuildProperties> buildProperties;

    @GetMapping
    public String health(Model model) {
        var health = healthEndpoint.health();

        // Overall status
        model.addAttribute("overallStatus", health.getStatus().getCode());

        // Component statuses
        Map<String, String> components = new LinkedHashMap<>();
        if (health instanceof SystemHealth systemHealth) {
            Map<String, HealthComponent> componentMap = systemHealth.getComponents();
            if (componentMap != null) {
                componentMap.forEach((name, component) -> {
                    if (component instanceof Health h) {
                        components.put(name, h.getStatus().getCode());
                    } else if (component instanceof CompositeHealth ch) {
                        components.put(name, ch.getStatus().getCode());
                    }
                });
            }
        }
        model.addAttribute("components", components);

        // App info
        buildProperties.ifPresent(bp -> {
            model.addAttribute("appVersion", bp.getVersion());
            model.addAttribute("appName", bp.getName());
        });

        // JVM uptime
        long uptimeMs = ManagementFactory.getRuntimeMXBean().getUptime();
        Duration uptime = Duration.ofMillis(uptimeMs);
        String uptimeStr = String.format("%dd %dh %dm %ds",
                uptime.toDays(), uptime.toHoursPart(), uptime.toMinutesPart(), uptime.toSecondsPart());
        model.addAttribute("uptime", uptimeStr);

        // JVM memory
        Runtime rt = Runtime.getRuntime();
        model.addAttribute("maxMemoryMB", rt.maxMemory() / 1024 / 1024);
        model.addAttribute("totalMemoryMB", rt.totalMemory() / 1024 / 1024);
        model.addAttribute("freeMemoryMB", rt.freeMemory() / 1024 / 1024);
        model.addAttribute("usedMemoryMB", (rt.totalMemory() - rt.freeMemory()) / 1024 / 1024);

        // Java info
        model.addAttribute("javaVersion", System.getProperty("java.version"));
        model.addAttribute("javaVendor", System.getProperty("java.vendor"));
        model.addAttribute("osName", System.getProperty("os.name"));
        model.addAttribute("osArch", System.getProperty("os.arch"));
        model.addAttribute("availableProcessors", rt.availableProcessors());

        return "admin/health";
    }
}

