package _bg.footballbettingapp.bet.model;

import _bg.footballbettingapp.match.model.Match;
import _bg.footballbettingapp.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "bets")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bet {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne (optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne (optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    @Column(name = "bet_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private BetType betType;

    @Column(name = "stake", nullable = false, precision = 19, scale = 2)
    private BigDecimal stake;

    @Column(name = "odds", nullable = false, precision = 5, scale = 2)
    private BigDecimal odds;

    @Column(name = "potential_winning_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal potentialWinningAmount;


    @Column(name = "bet_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private BetStatus betStatus;

    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;


    @Column(name = "settled_on")
    private LocalDateTime settledOn;

}
