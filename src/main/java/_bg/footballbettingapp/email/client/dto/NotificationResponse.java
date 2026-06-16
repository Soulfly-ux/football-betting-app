package _bg.footballbettingapp.email.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponse {

    private UUID id;

    private String title;

    private String message;

    private String type;

    private String status;

    private LocalDateTime createdOn;

    private Boolean isRead;
}
