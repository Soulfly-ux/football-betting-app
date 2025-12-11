package _bg.footballbettingapp.web.dto;

import _bg.footballbettingapp.common.model.Country;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.URL;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EditProfileRequest {

    @Size(max = 20, message = "First name must be between 2 and 20 characters")
    private String firstName;

    @Size(max = 20, message = "Last name must be between 2 and 20 characters")
    private String lastName;

    private Country country;

    @URL(message = "Profile picture url must be a valid URL")
    private String profilePictureUrl;
}
