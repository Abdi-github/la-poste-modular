package ch.swiftapp.laposte.delivery.web;

import ch.swiftapp.laposte.delivery.dto.request.CreatePickupRequest;
import ch.swiftapp.laposte.delivery.dto.response.PickupResponse;
import ch.swiftapp.laposte.delivery.enums.PickupStatus;
import ch.swiftapp.laposte.delivery.service.PickupRequestService;
import ch.swiftapp.laposte.user.UserModuleApi;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/pickups")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','BRANCH_MANAGER')")
public class PickupRequestWebController {

    private final PickupRequestService pickupRequestService;
    private final UserModuleApi userModuleApi;

    @GetMapping
    public String list(Model model,
                       @RequestParam(required = false) PickupStatus status,
                       @PageableDefault(size = 20) Pageable pageable) {
        model.addAttribute("page", pickupRequestService.getAll(status, pageable));
        model.addAttribute("statuses", PickupStatus.values());
        model.addAttribute("selectedStatus", status);
        return "pickups/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        PickupResponse pickup = pickupRequestService.getById(id);
        model.addAttribute("pickup", pickup);
        // Resolve customer name
        userModuleApi.findCustomerById(pickup.getCustomerId())
                .ifPresent(c -> model.addAttribute("customerName", c.getFirstName() + " " + c.getLastName()));
        // Available status transitions
        model.addAttribute("nextStatuses", getNextStatuses(pickup.getStatus()));
        return "pickups/detail";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("customers", userModuleApi.getAllCustomers(Pageable.unpaged()).getContent());
        return "pickups/form";
    }

    @PostMapping
    public String create(@ModelAttribute CreatePickupRequest request, RedirectAttributes ra) {
        try {
            PickupResponse created = pickupRequestService.create(request);
            ra.addFlashAttribute("successMessage", "Pickup request #" + created.getId() + " created");
            return "redirect:/pickups/" + created.getId();
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/pickups/new";
        }
    }

    @PostMapping("/{id}/status")
    public String changeStatus(@PathVariable Long id, @RequestParam PickupStatus newStatus, RedirectAttributes ra) {
        try {
            pickupRequestService.changeStatus(id, newStatus);
            ra.addFlashAttribute("successMessage", "Status changed to " + newStatus);
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/pickups/" + id;
    }

    private PickupStatus[] getNextStatuses(PickupStatus current) {
        return switch (current) {
            case PENDING -> new PickupStatus[]{PickupStatus.REQUESTED, PickupStatus.CANCELLED};
            case REQUESTED -> new PickupStatus[]{PickupStatus.CONFIRMED, PickupStatus.CANCELLED};
            case CONFIRMED -> new PickupStatus[]{PickupStatus.ASSIGNED, PickupStatus.CANCELLED};
            case ASSIGNED -> new PickupStatus[]{PickupStatus.PICKED_UP, PickupStatus.CANCELLED};
            case PICKED_UP, CANCELLED -> new PickupStatus[]{};
        };
    }
}

