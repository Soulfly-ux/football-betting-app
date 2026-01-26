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

    List<Match> findAllByMatchStatusInOrderByStartTimeAsc(List<MatchStatus> status);

    List<Match> findAllByMatchStatusAndStartTimeAfterOrderByStartTimeAsc(MatchStatus status, LocalDateTime startTime);

    List<Match> findAllByHomeTeamOrAwayTeam(Team homeTeam, Team awayTeam);

    boolean existsByHomeTeamAndAwayTeamAndStartTime(Team homeTeam, Team awayTeam, LocalDateTime startTime);

    Optional<Match> findById(UUID matchId);


   long countByMatchStatus(MatchStatus matchStatus);

   List<Match> findAllByMatchStatusAndStartTimeLessThanEqual(MatchStatus matchStatus, LocalDateTime now);

   long countByMatchStatusAndStartTimeBefore(MatchStatus matchStatus, LocalDateTime now);

   List<Match> findAllByMatchStatusOrderByStartTimeAsc(MatchStatus matchStatus);

   List<Match> findAllByMatchStatusOrderByStartTimeDesc(MatchStatus matchStatus);

   List<Match> findAllByMatchStatusAndStartTimeBeforeOrderByStartTimeAsc(MatchStatus matchStatus, LocalDateTime now);

   List<Match> findAllByMatchStatusAndStartTimeIsGreaterThanEqual(MatchStatus matchStatus, LocalDateTime startTime);







}
