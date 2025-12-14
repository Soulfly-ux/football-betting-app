package _bg.footballbettingapp.web.dto;

import _bg.footballbettingapp.bet.model.BetType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class BetRequest {

    @NotNull
    private UUID matchId;
    @NotNull
    private BetType betType;
    @NotNull
    @DecimalMin(value = "0.10", message = "Stake must be at least 0.10")
    private BigDecimal stake;
}
