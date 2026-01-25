package ch.swiftapp.laposte.shared.event;

public record EmployeeStatusChangedEvent(Long employeeId, String oldStatus, String newStatus) {}

