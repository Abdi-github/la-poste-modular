package ch.swiftapp.laposte.shared.event;

// ── User Module Events ──
public record UserCreatedEvent(Long userId, String email, String userType) {}

