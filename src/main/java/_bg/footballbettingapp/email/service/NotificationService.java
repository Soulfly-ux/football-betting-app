package _bg.footballbettingapp.email.service;

import _bg.footballbettingapp.email.client.NotificationClient;
import _bg.footballbettingapp.email.client.dto.NotificationRequest;
import _bg.footballbettingapp.email.client.dto.NotificationResponse;
import _bg.footballbettingapp.email.client.dto.UnreadNotificationCountResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class NotificationService {

    private static final String EMAIL_NOTIFICATION_TYPE = "EMAIL";

    private final NotificationClient notificationClient;

    public NotificationService(NotificationClient notificationClient) {
        this.notificationClient = notificationClient;
    }

    public void createNotification(UUID userId, String title, String message) {
        NotificationRequest notificationRequest = NotificationRequest.builder()
                .userId(userId)
                .title(title)
                .message(message)
                .type(EMAIL_NOTIFICATION_TYPE)
                .build();

        ResponseEntity<NotificationResponse> response = notificationClient.create(notificationRequest);

        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error("Notification could not be created for user with id [{}].", userId);
        }
    }

    public List<NotificationResponse> getNotifications(UUID userId) {
        ResponseEntity<List<NotificationResponse>> response = notificationClient.getNotificationsByUser(userId);

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            log.error("Notifications could not be fetched for user with id [{}].", userId);
            return Collections.emptyList();
        }

        return response.getBody();
    }


    public void markAsRead(UUID notificationId) {
        ResponseEntity<NotificationResponse> response = notificationClient.markAsRead(notificationId);

        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error("Notification with id [{}] could not be marked as read.", notificationId);
        }
    }


    public void deleteNotification(UUID notificationId) {
        ResponseEntity<Void> response = notificationClient.delete(notificationId);

        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error("Notification with id [{}] could not be deleted.", notificationId);
        }
    }

    public long getUnreadNotificationCount(UUID userId) {
        ResponseEntity<UnreadNotificationCountResponse> response = notificationClient.getUnreadNotificationCount(userId);

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            log.error("Unread notification count could not be fetched for user with id [{}].", userId);
            return 0;
        }

        return response.getBody().getCount();
    }
}
