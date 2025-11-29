package _bg.footballbettingapp.bet.service;

import _bg.footballbettingapp.bet.model.Bet;
import _bg.footballbettingapp.bet.model.BetStatus;
import _bg.footballbettingapp.bet.model.BetType;
import _bg.footballbettingapp.bet.repository.BetRepository;
import _bg.footballbettingapp.exception.DomainException;
import _bg.footballbettingapp.match.model.Match;
import _bg.footballbettingapp.match.model.MatchStatus;
import _bg.footballbettingapp.match.service.MatchService;
import _bg.footballbettingapp.user.model.User;
import _bg.footballbettingapp.user.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class BetService {


    private final BetRepository betRepository;
    private final MatchService matchService;
    private final UserService userService;
    @Autowired
    public BetService(BetRepository betRepository, MatchService matchService, UserService userService) {
        this.betRepository = betRepository;
        this.matchService = matchService;
        this.userService = userService;
    }
    @Transactional
    public Bet placeBet(UUID userId, UUID matchId, BetType betType, BigDecimal stake) {

        User user = userService.getUserById(userId);
        Match match = matchService.getMatchById(matchId);


        validateStake(stake);
        validateUserCanBet(user, stake);
        validateMatchCanBeBetOn(match);


        BigDecimal odds = resolveOdds(match, betType);


        BigDecimal potentialWin = stake
                .multiply(odds).setScale(2, RoundingMode.HALF_UP);

        Bet bet = Bet.builder()
                .user(user)
                .match(match)
                .betType(betType)
                .stake(stake)
                .odds(odds)
                .potentialWinningAmount(potentialWin)
                .betStatus(BetStatus.PENDING)
                .createdOn(LocalDateTime.now())
                .build();

        user.setBalance(user.getBalance().subtract(stake));
        userService.save(user);

        return betRepository.save(bet);
    }


    public List<Bet> getBetsByUser(UUID userId) {
        User user = userService.getUserById(userId);
        return betRepository.findAllByUserOrderByCreatedOnDesc(user);
    }


    private void validateStake(BigDecimal stake) {
        if(stake == null || stake.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DomainException("Stake must be greater than 0");
        }
    }


    private void validateUserCanBet(User user, BigDecimal stake) {

        if(user == null) {

            throw new DomainException("User not found");
        }

        if(user.getBalance().compareTo(stake) < 0) {

            throw new DomainException("User does not have enough balance");
        }

        if (!user.isActive()) {
            throw new DomainException("User is not active");
        }
    }


    private void validateMatchCanBeBetOn(Match match) {
        if(match == null) {
            throw new DomainException("Match not found");
        }

        if(match.getMatchStatus() != MatchStatus.SCHEDULED) {
            throw new DomainException("Match is not scheduled");
        }

        if(match.getStartTime().isBefore(LocalDateTime.now())) {
            throw new DomainException("Can't bet after match start");
        }

        if  (match.getOddHome() == null ||
             match.getOddDraw() == null  ||
             match.getOddAway() == null) {
            throw new DomainException("Match odds are not set");
        }
    }







    private BigDecimal resolveOdds(Match match, BetType betType) {
        return switch (betType) {
            case HOME_WIN -> match.getOddHome();
            case DRAW -> match.getOddDraw();
            case AWAY_WIN -> match.getOddAway();
        };
    }

    private BetType resolveBetType(int homeGoals, int awayGoals) {
        if (homeGoals > awayGoals) {
            return BetType.HOME_WIN;
        } else if (homeGoals < awayGoals) {
            return BetType.AWAY_WIN;
        } else {
            return BetType.DRAW;
        }

    }
    @Transactional
    public void settleBetForMatch(UUID matchId) {

        Match match = matchService.getMatchById(matchId);

        if (match.getMatchStatus() != MatchStatus.FINISHED) {
            throw new DomainException("Cannot settle bet. Match is not finished");
        }


        Integer homeGoals = match.getHomeGoals();
        Integer awayGoals = match.getAwayGoals();

        if (homeGoals == null || awayGoals == null) {
            throw new DomainException("N/A");
        }


        BetType betType = resolveBetType(homeGoals, awayGoals);


        List<Bet> pendingBets = betRepository.findAllByMatchAndBetStatus(match, BetStatus.PENDING);

        LocalDateTime now = LocalDateTime.now();


        for (Bet bet : pendingBets) {
            User user = bet.getUser();

            if (bet.getBetType() == betType) {
                bet.setBetStatus(BetStatus.WON);
                bet.setSettledOn(now);


                user.setBalance(user.getBalance().add(bet.getPotentialWinningAmount()));

                userService.save(user);
            }else {
                bet.setBetStatus(BetStatus.LOST);
                bet.setSettledOn(now);
            }
            betRepository.save(bet);

            }


        }

    }


