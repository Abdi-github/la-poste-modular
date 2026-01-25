package ch.swiftapp.laposte.shared.event;

public record ParcelCancelledEvent(Long parcelId, String trackingNumber) {}

