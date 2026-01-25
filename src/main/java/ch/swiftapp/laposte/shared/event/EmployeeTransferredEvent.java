package ch.swiftapp.laposte.shared.event;

public record EmployeeTransferredEvent(Long employeeId, Long fromBranchId, Long toBranchId) {}

