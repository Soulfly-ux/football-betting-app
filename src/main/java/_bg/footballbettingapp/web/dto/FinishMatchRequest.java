package _bg.footballbettingapp.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class FinishMatchRequest {

    @NotNull
    @Min(value = 0, message = "Home goals cannot be negative")
    private int homeGoals;

    @NotNull
    @Min(value = 0, message = "Away goals cannot be negative")
    private int awayGoals;
}
