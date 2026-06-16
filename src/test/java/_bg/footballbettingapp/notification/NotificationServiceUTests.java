package _bg.footballbettingapp.notification;

import _bg.footballbettingapp.email.client.NotificationClient;
import _bg.footballbettingapp.email.client.dto.NotificationResponse;
import _bg.footballbettingapp.email.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import javax.management.Notification;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;
@ExtendWith(MockitoExtension.class)
public class NotificationServiceUTests {

    @Mock
    private  NotificationClient notificationClient;

    @InjectMocks
    private NotificationService notificationService;



    @Test
    void givenSuccessfulResponseWithBody_whenGetNotifications_thenReturnNotifications() {

        UUID userId = UUID.randomUUID();
        NotificationResponse notification1 = NotificationResponse.builder().build();
        NotificationResponse notification2 = NotificationResponse.builder().build();

        List<NotificationResponse> notifications = List.of(notification1, notification2);

        ResponseEntity<List<NotificationResponse>> response = ResponseEntity.ok(notifications);

        when(notificationClient.getNotificationsByUser(userId)).thenReturn(response);

        List<NotificationResponse> result = notificationService.getNotifications(userId);

        assertEquals(notifications,result);
        verify(notificationClient).getNotificationsByUser(userId);


    }
}
