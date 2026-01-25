package _bg.footballbettingapp.match.service;

import _bg.footballbettingapp.bet.model.Bet;
import _bg.footballbettingapp.bet.model.BetStatus;
import _bg.footballbettingapp.bet.service.BetService;
import _bg.footballbettingapp.exception.DomainException;
import _bg.footballbettingapp.exception.MatchCancelException;
import _bg.footballbettingapp.exception.MatchCreateException;
import _bg.footballbettingapp.exception.MatchEditException;
import _bg.footballbettingapp.match.model.Match;
import _bg.footballbettingapp.match.model.MatchStatus;
import _bg.footballbettingapp.match.repository.MatchRepository;
import _bg.footballbettingapp.team.model.Team;
import _bg.footballbettingapp.team.service.TeamService;
import _bg.footballbettingapp.user.model.User;
import _bg.footballbettingapp.user.service.UserService;
import _bg.footballbettingapp.web.dto.CreateMatchRequest;
import _bg.footballbettingapp.web.dto.EditMatchRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
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
    private final UserService userService;
    private static final List<String> LEAGUES = List.of("Premier League",
            "La Liga", "Bundesliga", "Serie A", "Ligue 1","Champions League","Europa League", "Bulgarian Efbet League");

    @Autowired
    public MatchAdminService(MatchRepository matchRepository, BetService betService, TeamService teamService, UserService userService) {
        this.matchRepository = matchRepository;
        this.betService = betService;
        this.teamService = teamService;
        this.userService = userService;
    }


    public List<Match> getAdminOpenMatches() {
        return matchRepository.findAllByMatchStatusInOrderByStartTimeAsc(List.of(MatchStatus.SCHEDULED, MatchStatus.IN_PROGRESS));
    }
    @CacheEvict(value = "upcomingMatches", allEntries = true)
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

    @CacheEvict(value = "upcomingMatches", allEntries = true)
    public void createNewMatch(CreateMatchRequest createMatchRequest) {

        UUID homeTeamId = createMatchRequest.getHomeTeamId();
        UUID awayTeamId = createMatchRequest.getAwayTeamId();

        Team homeTeam = teamService.getById(homeTeamId);
        Team awayTeam = teamService.getById(awayTeamId);


        if (homeTeamId.equals(awayTeamId)) {
            throw new MatchCreateException("Home and away teams cannot be the same");
        }

        LocalDateTime now = LocalDateTime.now();

        if (createMatchRequest.getStartTime().isBefore(now.minusMinutes(1))) {
            throw new MatchCreateException("Match start time cannot be in the past");
        }

        BigDecimal oddHome = createMatchRequest.getOddHome();
        BigDecimal oddDraw = createMatchRequest.getOddDraw();
        BigDecimal oddAway = createMatchRequest.getOddAway();

        if (oddHome.compareTo(new BigDecimal("1.00")) < 0 || oddDraw.compareTo(new BigDecimal("1.00")) < 0
                || oddAway.compareTo(new BigDecimal("1.00")) < 0) {
            throw new MatchCreateException("Odds must be positive");
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


    private Match getMatchById(UUID matchId) {
        return matchRepository.findById(matchId).orElseThrow(() -> new DomainException("Match not found"));
    }


    public EditMatchRequest mapToEditMatchRequest (Match match) {
       //  за GET заявката - взима данните за редактиране -> GET edit = “дай ми текущите данни, за да ги редактирам”
//      мапва текущите данни на мача към дто, може да се направи в отделен мапър


        EditMatchRequest dto = new EditMatchRequest();
        dto.setStartTime(match.getStartTime());
        dto.setOddHome(match.getOddHome());
        dto.setOddDraw(match.getOddDraw());
        dto.setOddAway(match.getOddAway());


        return dto;
    }

    @CacheEvict(value = "upcomingMatches", allEntries = true)
    public void editMatch(EditMatchRequest dto, UUID matchId) {
       // за PUT заявката -> PUT edit = “ето новите данни, запази ги”

        Match match = getMatchById(matchId);

        LocalDateTime now = LocalDateTime.now();

        if (match.getMatchStatus() == MatchStatus.FINISHED || match.getMatchStatus() == MatchStatus.CANCELLED || match.getMatchStatus() == MatchStatus.IN_PROGRESS) {
            throw new MatchEditException(matchId,"Match cannot be edited as it is finished, cancelled or in progress");
        }

        if (!match.getStartTime().isAfter(now)) {
            throw new MatchEditException(matchId,"Match cannot be edited as it is in the past");
        }// не може да се редактира вече започнал мач

        if (!dto.getStartTime().isAfter(now)) {
            throw new MatchEditException(matchId,"Cannot set start time in the past");
        }// не може да се задава стартов час в миналото

        match.setStartTime(dto.getStartTime());
        match.setOddHome(dto.getOddHome());
        match.setOddDraw(dto.getOddDraw());
        match.setOddAway(dto.getOddAway());


        matchRepository.save(match);

    }
    @CacheEvict(value = "upcomingMatches", allEntries = true)
    @Transactional
    public void cancelMatch(UUID matchId) {

        Match match = getMatchById(matchId);




        if (match.getMatchStatus() == MatchStatus.FINISHED) {
            throw new MatchCancelException(matchId, "Match cannot be cancelled after it has finished");
        }


        if (match.getMatchStatus() == MatchStatus.CANCELLED) {
            throw new MatchCancelException(matchId, "Match is already cancelled");
        }

        if (match.getMatchStatus() == MatchStatus.IN_PROGRESS) {
            throw new MatchCancelException(matchId, "Cannot cancel match in progress");
        }



        List<Bet> pendingBets = betService.getBetsByMatchAndStatus(match, BetStatus.PENDING);


        LocalDateTime now = LocalDateTime.now();


        for (Bet bet : pendingBets) {

            User user = bet.getUser();

            user.setBalance(user.getBalance().add(bet.getStake()));
            bet.setBetStatus(BetStatus.CANCELLED);
            bet.setSettledOn(now);

            userService.save(user);
            betService.save(bet);

        }


        match.setMatchStatus(MatchStatus.CANCELLED);
        matchRepository.save(match);

    }


    public long countAll() {
        return matchRepository.count();
    }

    public long countByStatus(MatchStatus status) {
        return matchRepository.countByMatchStatus(status);
    }
    @CacheEvict(value = "upcomingMatches", allEntries = true)
    @Transactional
    public void markScheduledMatchesAsInProgress() {
        LocalDateTime now = LocalDateTime.now();

        List<Match> scheduledMatches = matchRepository.findAllByMatchStatusAndStartTimeLessThanEqual(MatchStatus.SCHEDULED, now);


        for (Match match : scheduledMatches) {
            match.setMatchStatus(MatchStatus.IN_PROGRESS);
            matchRepository.save(match);
        }
    }

    public long countOverdueMatches() {
        LocalDateTime now = LocalDateTime.now();


        return matchRepository.countByMatchStatusAndStartTimeBefore(MatchStatus.SCHEDULED, now);
    }
}










