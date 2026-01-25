package ch.swiftapp.laposte.shared.event;

public record ParcelCreatedEvent(Long parcelId, String trackingNumber, Long senderCustomerId) {}

