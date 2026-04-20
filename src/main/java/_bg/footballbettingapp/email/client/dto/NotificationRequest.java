package _bg.footballbettingapp.email.client.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class NotificationRequest {


    private UUID userId;


    private String title;


    private String message;


    private String type;
}
