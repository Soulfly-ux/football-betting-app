package _bg.footballbettingapp.email.client.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class NotificationResponse {

    private UUID id;

    private String title;

    private String message;

    private String type;

    private String status;

    private LocalDateTime createdOn;

    private Boolean isRead;
}
