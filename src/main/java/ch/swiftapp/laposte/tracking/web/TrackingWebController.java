package ch.swiftapp.laposte.tracking.web;

import ch.swiftapp.laposte.tracking.service.TrackingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j @Controller @RequestMapping("/tracking") @RequiredArgsConstructor
public class TrackingWebController {
    private final TrackingService trackingService;

    @GetMapping
    public String search(Model model, @RequestParam(required = false) String trackingNumber) {
        model.addAttribute("trackingNumber", trackingNumber);
        if (trackingNumber != null && !trackingNumber.isBlank()) {
            try {
                model.addAttribute("timeline", trackingService.getTimeline(trackingNumber.trim()));
            } catch (Exception e) {
                log.warn("Tracking not found: {}", trackingNumber);
                model.addAttribute("errorMessage", "Tracking number not found: " + trackingNumber);
            }
        }
        return "tracking/search";
    }
}

