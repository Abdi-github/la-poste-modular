package ch.swiftapp.laposte.dashboard.web;

import ch.swiftapp.laposte.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller @RequiredArgsConstructor
public class DashboardWebController {
    private final DashboardService dashboardService;

    @GetMapping("/")
    public String home() {
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalParcels", dashboardService.totalParcels());
        model.addAttribute("activeDeliveries", dashboardService.activeDeliveries());
        model.addAttribute("totalBranches", dashboardService.totalBranches());
        model.addAttribute("totalEmployees", dashboardService.totalEmployees());
        model.addAttribute("totalCustomers", dashboardService.totalCustomers());
        model.addAttribute("pendingDeliveries", dashboardService.pendingDeliveries());
        model.addAttribute("totalNotifications", dashboardService.totalNotifications());
        model.addAttribute("recentParcels", dashboardService.recentParcels());
        model.addAttribute("recentNotifications", dashboardService.recentNotifications());
        // Chart data
        model.addAttribute("parcelsByStatus", dashboardService.parcelsByStatus());
        model.addAttribute("deliveriesByStatus", dashboardService.deliveriesByStatus());
        return "dashboard/index";
    }
}

