package _bg.footballbettingapp.web.dto;

import _bg.footballbettingapp.common.model.Country;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EditProfileRequest {

    private String firstName;
    private String lastName;
    private Country country;
    private String profilePictureUrl;
}
