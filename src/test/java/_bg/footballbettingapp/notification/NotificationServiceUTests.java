package _bg.footballbettingapp.notification;

import _bg.footballbettingapp.email.client.NotificationClient;
import _bg.footballbettingapp.email.client.dto.NotificationResponse;
import _bg.footballbettingapp.email.client.dto.UnreadNotificationCountResponse;
import _bg.footballbettingapp.email.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import javax.management.Notification;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    @Test
    void givenSuccessfulResponseWithNullBody_whenGetNotifications_thenReturnEmptyList() {
        UUID userId = UUID.randomUUID();

        ResponseEntity<List<NotificationResponse>> response = ResponseEntity.ok(null);

        when(notificationClient.getNotificationsByUser(userId)).thenReturn(response);

        List<NotificationResponse> result = notificationService.getNotifications(userId);

        assertTrue(result.isEmpty());
        verify(notificationClient).getNotificationsByUser(userId);

    }

    @Test
    void givenClientThrowsException_whenGetNotifications_thenReturnEmptyList() {
        UUID userId = UUID.randomUUID();

        when(notificationClient.getNotificationsByUser(userId)).thenThrow(new RuntimeException());

        List<NotificationResponse> result = notificationService.getNotifications(userId);

        assertTrue(result.isEmpty());
        verify(notificationClient).getNotificationsByUser(userId);

    }

    @Test
    void givenSuccessfulResponseWithBody_whenGetUnreadNotificationCount_thenReturnCount() {

        UUID userId = UUID.randomUUID();

        UnreadNotificationCountResponse body = new UnreadNotificationCountResponse();
        body.setCount(2);

        ResponseEntity<UnreadNotificationCountResponse> response = ResponseEntity.ok(body);
        when(notificationClient.getUnreadNotificationCount(userId)).thenReturn(response);

        long result = notificationService.getUnreadNotificationCount(userId);

        assertEquals(2, result);
        verify(notificationClient).getUnreadNotificationCount(userId);
    }


    @Test
    void givenSuccessfulResponseWithNullBody_whenGetUnreadNotificationCount_thenReturnZero() {

        UUID userId = UUID.randomUUID();



        ResponseEntity<UnreadNotificationCountResponse> response = ResponseEntity.ok(null);
        when(notificationClient.getUnreadNotificationCount(userId)).thenReturn(response);

        long result = notificationService.getUnreadNotificationCount(userId);

        assertEquals(0, result);
        verify(notificationClient).getUnreadNotificationCount(userId);
    }


    @Test
    void givenClientThrowsException_whenGetUnreadNotificationCount_thenReturnZero() {

        UUID userId = UUID.randomUUID();

        when(notificationClient.getUnreadNotificationCount(userId)).thenThrow(new RuntimeException());

        long result = notificationService.getUnreadNotificationCount(userId);

        assertEquals(0, result);
        verify(notificationClient).getUnreadNotificationCount(userId);



    }
 }
