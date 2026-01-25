package ch.swiftapp.laposte.shared.event;

public record ParcelScannedEvent(String trackingNumber, String eventType, Long branchId) {}

