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

        ResponseEntity<NotificationResponse> response;
        try {
            response = notificationClient.create(notificationRequest);
            if (!response.getStatusCode().is2xxSuccessful()) {
                log.error("Notification could not be created for user with id [{}].", userId);
            }
        } catch (Exception e) {
            log.warn("Can't create notification to user id [{}]. Bet flow will continue.", userId, e);

        }
    }

    public List<NotificationResponse> getNotifications(UUID userId) {
        try {
            ResponseEntity<List<NotificationResponse>> response = notificationClient.getNotificationsByUser(userId);

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                log.error("Notifications could not be fetched for user with id [{}].", userId);
                return Collections.emptyList();
            }

            return response.getBody();
        } catch (Exception e) {
            log.warn("Can't fetch notifications for user id [{}]. Empty list will be shown.", userId, e);
            return Collections.emptyList();
        }
    }


    public void markAsRead(UUID notificationId) {
        try {
            ResponseEntity<NotificationResponse> response = notificationClient.markAsRead(notificationId);

            if (!response.getStatusCode().is2xxSuccessful()) {
                log.error("Notification with id [{}] could not be marked as read.", notificationId);
            }
        } catch (Exception e) {
            log.warn("Can't mark notification with id [{}] as read.", notificationId, e);
        }
    }


    public void deleteNotification(UUID notificationId) {
        try {
            ResponseEntity<Void> response = notificationClient.delete(notificationId);

            if (!response.getStatusCode().is2xxSuccessful()) {
                log.error("Notification with id [{}] could not be deleted.", notificationId);
            }
        } catch (Exception e) {
            log.warn("Can't delete notification with id [{}].", notificationId, e);
        }
    }

    public long getUnreadNotificationCount(UUID userId) {
        try {
            ResponseEntity<UnreadNotificationCountResponse> response = notificationClient.getUnreadNotificationCount(userId);

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                log.error("Unread notification count could not be fetched for user with id [{}].", userId);
                return 0;
            }

            return response.getBody().getCount();
        } catch (Exception e) {
            log.warn("Can't fetch unread notification count for user id [{}]. Zero will be shown.", userId, e);
            return 0;
        }
    }
}
