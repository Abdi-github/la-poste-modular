package ch.swiftapp.laposte.shared.web;

import ch.swiftapp.laposte.branch.BranchModuleApi;
import ch.swiftapp.laposte.delivery.DeliveryModuleApi;
import ch.swiftapp.laposte.parcel.ParcelModuleApi;
import ch.swiftapp.laposte.user.UserModuleApi;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;

/**
 * Admin reports page with CSV export for all major entities.
 */
@Slf4j
@Controller
@RequestMapping("/admin/reports")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class ReportWebController {

    private final ParcelModuleApi parcelModuleApi;
    private final UserModuleApi userModuleApi;
    private final BranchModuleApi branchModuleApi;

    @GetMapping
    public String reportsPage() {
        return "admin/reports";
    }

    @GetMapping("/{entity}/csv")
    public void exportCsv(@PathVariable String entity, HttpServletResponse response) throws IOException {
        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition",
                "attachment; filename=\"" + entity + "-" + LocalDate.now() + ".csv\"");

        PrintWriter writer = response.getWriter();
        switch (entity) {
            case "parcels" -> exportParcels(writer);
            case "employees" -> exportEmployees(writer);
            case "customers" -> exportCustomers(writer);
            case "branches" -> exportBranches(writer);
            default -> {
                writer.println("Unknown entity: " + entity);
            }
        }
        writer.flush();
    }

    private void exportParcels(PrintWriter w) {
        w.println("ID,Tracking Number,Type,Status,Weight (kg),Price,Sender,Recipient,Created");
        var page = parcelModuleApi.getAll(null, Pageable.unpaged());
        page.getContent().forEach(p -> w.printf("%d,%s,%s,%s,%.2f,%.2f,%s,%s,%s%n",
                p.getId(), p.getTrackingNumber(), p.getType(), p.getStatus(),
                p.getWeightKg() != null ? p.getWeightKg() : 0,
                p.getPrice() != null ? p.getPrice().doubleValue() : 0,
                csvEscape(p.getSenderName()), csvEscape(p.getRecipientName()),
                p.getCreatedAt()));
    }

    private void exportEmployees(PrintWriter w) {
        w.println("ID,Employee Number,First Name,Last Name,Email,Role,Status,Branch ID");
        var page = userModuleApi.getAllEmployees(Pageable.unpaged());
        page.getContent().forEach(e -> w.printf("%d,%s,%s,%s,%s,%s,%s,%s%n",
                e.getId(), e.getEmployeeNumber(), csvEscape(e.getFirstName()),
                csvEscape(e.getLastName()), e.getEmail(), e.getRole(), e.getStatus(),
                e.getAssignedBranchId() != null ? e.getAssignedBranchId() : ""));
    }

    private void exportCustomers(PrintWriter w) {
        w.println("ID,Customer Number,First Name,Last Name,Email,Phone,Status");
        var page = userModuleApi.getAllCustomers(Pageable.unpaged());
        page.getContent().forEach(c -> w.printf("%d,%s,%s,%s,%s,%s,%s%n",
                c.getId(), c.getCustomerNumber(), csvEscape(c.getFirstName()),
                csvEscape(c.getLastName()), c.getEmail(),
                c.getPhone() != null ? c.getPhone() : "", c.getStatus()));
    }

    private void exportBranches(PrintWriter w) {
        w.println("ID,Branch Code,Name,Type,Status,City,Canton,ZIP");
        branchModuleApi.findAll().forEach(b -> w.printf("%d,%s,%s,%s,%s,%s,%s,%s%n",
                b.getId(), b.getBranchCode(), csvEscape(b.getName()), b.getType(), b.getStatus(),
                csvEscape(b.getCity()), b.getCanton() != null ? b.getCanton() : "", b.getZipCode()));
    }

    private String csvEscape(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}

