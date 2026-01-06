package _bg.footballbettingapp.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class CreateMatchRequest {

    @NotNull
    private UUID homeTeamId;
    @NotNull
    private UUID awayTeamId;
    @NotNull
    private LocalDateTime startTime;
    @NotBlank
    private String leagueName;
    @NotNull
    @DecimalMin("1.01")
    private BigDecimal oddHome;
    @NotNull
    @DecimalMin("1.01")
    private BigDecimal oddAway;
    @NotNull
    @DecimalMin("1.01")
    private BigDecimal oddDraw;




}
