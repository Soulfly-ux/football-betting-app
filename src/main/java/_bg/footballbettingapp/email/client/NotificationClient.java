package _bg.footballbettingapp.email.client;

import _bg.footballbettingapp.email.client.dto.NotificationRequest;
import _bg.footballbettingapp.email.client.dto.NotificationResponse;
import _bg.footballbettingapp.email.client.dto.UnreadNotificationCountResponse;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "notification-svc", url = "http://localhost:8081/api/v1/notifications")
public interface NotificationClient {

    @PostMapping
    ResponseEntity<NotificationResponse> create(@Valid @RequestBody NotificationRequest notificationRequest);


    @GetMapping("/users/{userId}")
    ResponseEntity<List<NotificationResponse>> getNotificationsByUser(@PathVariable("userId") UUID userId);


    @PatchMapping("/{notificationId}/read")
    ResponseEntity<NotificationResponse> markAsRead(@PathVariable("notificationId") UUID notificationId);

    @DeleteMapping("/{notificationId}")
    ResponseEntity<Void> delete(@PathVariable("notificationId") UUID notificationId);


    @GetMapping("/users/{userId}/unread-count")
    ResponseEntity<UnreadNotificationCountResponse> getUnreadNotificationCount(@PathVariable("userId") UUID userId);
}
