package _bg.footballbettingapp.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class RegisterRequest {


    @Size(min = 6, message = "Username must be at least 6 characters long")
    private String username;

    @Email(message = "Requires a valid email address")
    private String email;
    @Size(min = 3, message = "Password must be at least 6 characters long")
    private String password;

    @Size(min = 3, message = "Password must be at least 6 characters long")
    private String confirmPassword;


}
