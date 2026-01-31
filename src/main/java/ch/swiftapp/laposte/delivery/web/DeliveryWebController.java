package ch.swiftapp.laposte.delivery.web;

import ch.swiftapp.laposte.branch.BranchModuleApi;
import ch.swiftapp.laposte.delivery.dto.request.CreateRouteRequest;
import ch.swiftapp.laposte.delivery.dto.response.RouteResponse;
import ch.swiftapp.laposte.delivery.enums.RouteStatus;
import ch.swiftapp.laposte.delivery.enums.SlotStatus;
import ch.swiftapp.laposte.delivery.service.DeliveryRouteService;
import ch.swiftapp.laposte.user.UserModuleApi;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/deliveries")
@RequiredArgsConstructor
public class DeliveryWebController {
    private final DeliveryRouteService deliveryRouteService;
    private final BranchModuleApi branchModuleApi;
    private final UserModuleApi userModuleApi;

    @GetMapping
    public String list(Model model,
                       @RequestParam(required = false) RouteStatus status,
                       @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                       @RequestParam(required = false) String search,
                       @PageableDefault(size = 20, sort = "date", direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable) {
        model.addAttribute("page", deliveryRouteService.getAll(null, status, date, search, pageable));
        model.addAttribute("statuses", RouteStatus.values());
        model.addAttribute("selectedStatus", status);
        model.addAttribute("selectedDate", date);
        model.addAttribute("search", search);
        return "deliveries/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        RouteResponse route = deliveryRouteService.getById(id);
        model.addAttribute("route", route);
        model.addAttribute("slotStatuses", SlotStatus.values());
        if (route.getBranchId() != null) {
            branchModuleApi.findById(route.getBranchId())
                    .ifPresent(b -> model.addAttribute("branchName", b.getName()));
        }
        if (route.getAssignedEmployeeId() != null) {
            userModuleApi.findEmployeeById(route.getAssignedEmployeeId())
                    .ifPresent(e -> model.addAttribute("courierName", e.getFirstName() + " " + e.getLastName()));
        }
        return "deliveries/detail";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("branches", branchModuleApi.findAll());
        model.addAttribute("employees", userModuleApi.getAllEmployees(Pageable.unpaged()).getContent());
        return "deliveries/form";
    }

    @PostMapping
    public String create(@RequestParam Long branchId,
                         @RequestParam(required = false) Long assignedEmployeeId,
                         @RequestParam LocalDate date,
                         @RequestParam String trackingNumbersText,
                         RedirectAttributes ra) {
        try {
            List<String> trackingNumbers = Arrays.stream(trackingNumbersText.split("\\s+"))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .toList();
            CreateRouteRequest request = CreateRouteRequest.builder()
                    .branchId(branchId)
                    .assignedEmployeeId(assignedEmployeeId)
                    .date(date)
                    .trackingNumbers(trackingNumbers)
                    .build();
            RouteResponse created = deliveryRouteService.create(request);
            ra.addFlashAttribute("successMessage", "Route " + created.getRouteCode() + " created");
            return "redirect:/deliveries/" + created.getId();
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/deliveries/new";
        }
    }

    @PostMapping("/{id}/start")
    public String start(@PathVariable Long id, RedirectAttributes ra) {
        try {
            deliveryRouteService.startRoute(id);
            ra.addFlashAttribute("successMessage", "Route started");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/deliveries/" + id;
    }

    @PostMapping("/{id}/complete")
    public String complete(@PathVariable Long id, RedirectAttributes ra) {
        try {
            deliveryRouteService.completeRoute(id);
            ra.addFlashAttribute("successMessage", "Route completed");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/deliveries/" + id;
    }

    @PostMapping("/{routeId}/slots/{slotId}/status")
    public String updateSlotStatus(@PathVariable Long routeId,
                                   @PathVariable Long slotId,
                                   @RequestParam SlotStatus newStatus,
                                   RedirectAttributes ra) {
        try {
            deliveryRouteService.updateSlotStatus(routeId, slotId, newStatus);
            ra.addFlashAttribute("successMessage", "Slot status updated to " + newStatus);
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/deliveries/" + routeId;
    }
}
