package _bg.footballbettingapp.match.service;

import _bg.footballbettingapp.bet.service.BetService;
import _bg.footballbettingapp.exception.DomainException;
import _bg.footballbettingapp.match.model.Match;
import _bg.footballbettingapp.match.model.MatchStatus;
import _bg.footballbettingapp.match.repository.MatchRepository;
import _bg.footballbettingapp.team.model.Team;
import _bg.footballbettingapp.team.service.TeamService;
import _bg.footballbettingapp.web.dto.CreateMatchRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class MatchAdminService {


    private final MatchRepository matchRepository;
    private final BetService betService;
    private final TeamService teamService;
    private static final List<String> LEAGUES = List.of("Premier League",
            "La Liga", "Bundesliga", "Serie A", "Ligue 1","Champions League","Europa League", "Bulgarian Efbet League");

    @Autowired
    public MatchAdminService(MatchRepository matchRepository, BetService betService, TeamService teamService) {
        this.matchRepository = matchRepository;
        this.betService = betService;
        this.teamService = teamService;
    }


    public List<Match> getAdminOpenMatches() {
        return matchRepository.findAllByMatchStatusInOrderByStartTimeAsc(List.of(MatchStatus.SCHEDULED, MatchStatus.IN_PROGRESS));
    }

    @Transactional
    public void finishMatch(UUID id, Integer homeGoals, Integer awayGoals) {
        Match match = matchRepository.findById(id)
                .orElseThrow(() -> new DomainException("Match not found with id: " + id));
       if (match.getMatchStatus() == MatchStatus.FINISHED){
           throw new DomainException("Match is already finished");
       }

        if (match.getMatchStatus() == MatchStatus.CANCELLED){
            throw new DomainException("Match is already cancelled");
        }


        match.setHomeGoals(homeGoals);
        match.setAwayGoals(awayGoals);
        match.setMatchStatus(MatchStatus.FINISHED);

        matchRepository.saveAndFlush(match);

        betService.settleBetForMatch(id);

    }

    public List<String> getSupportedLeagues() {
        return LEAGUES;
    }


    public void createNewMatch(CreateMatchRequest createMatchRequest) {

        UUID homeTeamId = createMatchRequest.getHomeTeamId();
        UUID awayTeamId = createMatchRequest.getAwayTeamId();

        Team homeTeam = teamService.getById(homeTeamId);
        Team awayTeam = teamService.getById(awayTeamId);


        if (homeTeamId.equals(awayTeamId)) {
            throw new DomainException("Home and away teams cannot be the same");
        }

        LocalDateTime now = LocalDateTime.now();

        if (createMatchRequest.getStartTime().isBefore(now.minusMinutes(1))) {
            throw new DomainException("Match start time cannot be in the past");
        }

        BigDecimal oddHome = createMatchRequest.getOddHome();
        BigDecimal oddDraw = createMatchRequest.getOddDraw();
        BigDecimal oddAway = createMatchRequest.getOddAway();

        if (oddHome.compareTo(new BigDecimal("1.00")) < 0 || oddDraw.compareTo(new BigDecimal("1.00")) < 0
                || oddAway.compareTo(new BigDecimal("1.00")) < 0) {
            throw new DomainException("Odds must be positive");
        }

        Match match = Match.builder()
                .homeTeam(homeTeam)
                .awayTeam(awayTeam)
                .startTime(createMatchRequest.getStartTime())
                .matchStatus(MatchStatus.SCHEDULED)
                .leagueName(createMatchRequest.getLeagueName())
                .oddHome(oddHome)
                .oddDraw(oddDraw)
                .oddAway(oddAway)
                .build();

        matchRepository.saveAndFlush(match);
    }
}
