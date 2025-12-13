package _bg.footballbettingapp.web.dto;

import _bg.footballbettingapp.bet.model.BetType;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class BetRequest {

    private UUID matchId;
    private BetType betType;
    private int amount;
}
