package ch.swiftapp.laposte.notification;

import ch.swiftapp.laposte.notification.dto.response.NotificationLogResponse;

import java.util.List;

/**
 * Public API for the Notification module. Other modules depend on this interface only.
 */
public interface NotificationModuleApi {
    List<NotificationLogResponse> recentNotifications(int limit);
    long countAll();
    int retryFailedNotifications();
}

