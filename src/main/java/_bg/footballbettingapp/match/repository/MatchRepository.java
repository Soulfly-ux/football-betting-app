package _bg.footballbettingapp.match.repository;

import _bg.footballbettingapp.bet.model.Bet;
import _bg.footballbettingapp.match.model.Match;
import _bg.footballbettingapp.match.model.MatchStatus;
import _bg.footballbettingapp.team.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface MatchRepository extends JpaRepository<Match, UUID> {

    List<Match> findAllByMatchStatusOrderByStartTimeAsc(MatchStatus status);

    List<Match> findAllByMatchStatusAndStartTimeAfterOrderByStartTimeAsc(MatchStatus status, LocalDateTime startTime);

    List<Match> findAllByHomeTeamOrAwayTeam(Team homeTeam, Team awayTeam);

    boolean existsByHomeTeamAndAwayTeamAndStartTime(Team homeTeam, Team awayTeam, LocalDateTime startTime);


}
