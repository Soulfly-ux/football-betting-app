package _bg.footballbettingapp.match.model;

import _bg.footballbettingapp.team.model.Team;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "matches")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Match {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne (optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "home_team_id", nullable = false)
    private Team homeTeam;


    @ManyToOne (optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "awey_team_id", nullable = false)
    private Team aweyTeam;


    private LocalDateTime startTime;

    @Column(name = "match_status")
    @Enumerated(EnumType.STRING)
    private MatchStatus matchStatus;

    @Column(name = "home_goals")
    private Integer homeGoals;

    @Column(name = "away_goals")
    private Integer awayGoals;

    @Column(name = "league_name", nullable = false, length = 100)
    private String leagueName;

    @Column(name = "odd_home", nullable = false, precision = 5, scale = 2)
    private BigDecimal oddHome;

    @Column(name = "odd_draw", nullable = false, precision = 5, scale = 2)
    private BigDecimal oddDraw;

    @Column(name = "odd_away", nullable = false, precision = 5, scale = 2)
    private BigDecimal oddAway;

}
