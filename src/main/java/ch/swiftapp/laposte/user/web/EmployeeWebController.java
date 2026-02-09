package ch.swiftapp.laposte.user.web;

import ch.swiftapp.laposte.branch.BranchModuleApi;
import ch.swiftapp.laposte.branch.dto.response.BranchResponse;
import ch.swiftapp.laposte.user.dto.request.CreateEmployeeRequest;
import ch.swiftapp.laposte.user.dto.request.UpdateEmployeeRequest;
import ch.swiftapp.laposte.user.dto.response.EmployeeResponse;
import ch.swiftapp.laposte.user.enums.EmployeeRole;
import ch.swiftapp.laposte.user.enums.EmployeeStatus;
import ch.swiftapp.laposte.user.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller @RequestMapping("/employees") @RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','BRANCH_MANAGER')")
public class EmployeeWebController {
    private final EmployeeService employeeService;
    private final BranchModuleApi branchModuleApi;

    @GetMapping
    public String list(Model model,
                       @RequestParam(required = false) EmployeeStatus status,
                       @RequestParam(required = false) EmployeeRole role,
                       @RequestParam(required = false) Long branchId,
                       @RequestParam(required = false) String search,
                       @PageableDefault(size = 20) Pageable pageable) {
        model.addAttribute("page", employeeService.getAll(status, role, branchId, search, pageable));
        model.addAttribute("statuses", EmployeeStatus.values());
        model.addAttribute("allRoles", EmployeeRole.values());
        model.addAttribute("allBranches", branchModuleApi.findAll());
        model.addAttribute("selectedStatus", status);
        model.addAttribute("selectedRole", role);
        model.addAttribute("selectedBranchId", branchId);
        model.addAttribute("search", search);
        return "employees/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        EmployeeResponse emp = employeeService.getById(id);
        model.addAttribute("employee", emp);
        if (emp.getAssignedBranchId() != null) {
            branchModuleApi.findById(emp.getAssignedBranchId())
                    .ifPresent(b -> model.addAttribute("branchName", b.getName()));
        }
        return "employees/detail";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("employee", CreateEmployeeRequest.builder().preferredLocale("de").build());
        model.addAttribute("roles", EmployeeRole.values());
        model.addAttribute("branches", branchModuleApi.findAll());
        model.addAttribute("editMode", false);
        return "employees/form";
    }

    @PostMapping
    public String create(@ModelAttribute("employee") CreateEmployeeRequest request, RedirectAttributes ra) {
        try {
            EmployeeResponse created = employeeService.create(request);
            ra.addFlashAttribute("successMessage", "Employee " + created.getEmployeeNumber() + " created");
            return "redirect:/employees/" + created.getId();
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/employees/new";
        }
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        EmployeeResponse emp = employeeService.getById(id);
        UpdateEmployeeRequest form = UpdateEmployeeRequest.builder()
                .firstName(emp.getFirstName())
                .lastName(emp.getLastName())
                .email(emp.getEmail())
                .phone(emp.getPhone())
                .role(emp.getRole())
                .assignedBranchId(emp.getAssignedBranchId())
                .hireDate(emp.getHireDate())
                .preferredLocale(emp.getPreferredLocale())
                .build();
        model.addAttribute("employee", form);
        model.addAttribute("employeeId", id);
        model.addAttribute("roles", EmployeeRole.values());
        model.addAttribute("branches", branchModuleApi.findAll());
        model.addAttribute("editMode", true);
        return "employees/form";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id, @ModelAttribute("employee") UpdateEmployeeRequest request, RedirectAttributes ra) {
        try {
            employeeService.update(id, request);
            ra.addFlashAttribute("successMessage", "Employee updated");
            return "redirect:/employees/" + id;
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/employees/" + id + "/edit";
        }
    }

    @PostMapping("/{id}/status")
    public String changeStatus(@PathVariable Long id, @RequestParam EmployeeStatus newStatus, RedirectAttributes ra) {
        try {
            employeeService.changeStatus(id, newStatus);
            ra.addFlashAttribute("successMessage", "Status changed to " + newStatus);
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/employees/" + id;
    }
}
