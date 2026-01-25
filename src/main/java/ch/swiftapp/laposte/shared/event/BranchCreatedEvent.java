package ch.swiftapp.laposte.shared.event;

public record BranchCreatedEvent(Long branchId, String branchCode, String type) {}

