package ch.swiftapp.laposte.user.web;

import ch.swiftapp.laposte.user.dto.request.CreateCustomerRequest;
import ch.swiftapp.laposte.user.dto.request.UpdateCustomerRequest;
import ch.swiftapp.laposte.user.dto.response.CustomerResponse;
import ch.swiftapp.laposte.user.enums.CustomerStatus;
import ch.swiftapp.laposte.user.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller @RequestMapping("/customers") @RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','BRANCH_MANAGER')")
public class CustomerWebController {
    private final CustomerService customerService;

    @GetMapping
    public String list(Model model,
                       @RequestParam(required = false) CustomerStatus status,
                       @RequestParam(required = false) String search,
                       @PageableDefault(size = 20) Pageable pageable) {
        model.addAttribute("page", customerService.getAll(status, search, pageable));
        model.addAttribute("statuses", CustomerStatus.values());
        model.addAttribute("selectedStatus", status);
        model.addAttribute("search", search);
        return "customers/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("customer", customerService.getById(id));
        return "customers/detail";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("customer", CreateCustomerRequest.builder().preferredLocale("de").build());
        model.addAttribute("editMode", false);
        return "customers/form";
    }

    @PostMapping
    public String create(@ModelAttribute("customer") CreateCustomerRequest request, RedirectAttributes ra) {
        try {
            CustomerResponse created = customerService.create(request);
            ra.addFlashAttribute("successMessage", "Customer " + created.getCustomerNumber() + " created");
            return "redirect:/customers/" + created.getId();
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/customers/new";
        }
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        CustomerResponse cust = customerService.getById(id);
        UpdateCustomerRequest form = UpdateCustomerRequest.builder()
                .firstName(cust.getFirstName())
                .lastName(cust.getLastName())
                .email(cust.getEmail())
                .phone(cust.getPhone())
                .preferredLocale(cust.getPreferredLocale())
                .build();
        model.addAttribute("customer", form);
        model.addAttribute("customerId", id);
        model.addAttribute("editMode", true);
        return "customers/form";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id, @ModelAttribute("customer") UpdateCustomerRequest request, RedirectAttributes ra) {
        try {
            customerService.update(id, request);
            ra.addFlashAttribute("successMessage", "Customer updated");
            return "redirect:/customers/" + id;
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/customers/" + id + "/edit";
        }
    }
}

