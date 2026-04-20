package _bg.footballbettingapp.bet.service;

import _bg.footballbettingapp.bet.model.Bet;
import _bg.footballbettingapp.bet.model.BetStatus;
import _bg.footballbettingapp.bet.model.BetType;
import _bg.footballbettingapp.bet.repository.BetRepository;
import _bg.footballbettingapp.email.service.NotificationService;
import _bg.footballbettingapp.exception.DomainException;
import _bg.footballbettingapp.exception.InsufficientBalanceException;
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
    private final NotificationService notificationService;
    @Autowired
    public BetService(BetRepository betRepository, MatchService matchService, UserService userService, NotificationService notificationService) {
        this.betRepository = betRepository;
        this.matchService = matchService;
        this.userService = userService;
        this.notificationService = notificationService;
    }
    @Transactional
    public Bet placeBet(UUID userId, UUID matchId, BetType betType, BigDecimal stake) {

        User user = userService.getUserById(userId);
        Match match = matchService.getMatchById(matchId);


        validateStake(stake);
        validateUserCanBet(user, stake, matchId);
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

        Bet savedBet = betRepository.save(bet);

         String title ="Bet placed successfully";
        String message = "Your bet on %s vs %s was placed successfully. Bet type: %s, stake: %s, odds: %s, potential win: %s."
                .formatted(
                        match.getHomeTeam().getName(),
                        match.getAwayTeam().getName(),
                        betType,
                        stake,
                        odds,
                        potentialWin
                );
        notificationService.createNotification(userId, title, message);


        return savedBet ;
    }


//    public List<Bet> getBetsByUser(UUID userId) {
//        User user = userService.getUserById(userId);
//        return betRepository.findAllByUserOrderByCreatedOnDesc(user);
//    }

    public List<Bet> getBetsByUser(User user) {
        return betRepository.findAllByUserOrderByCreatedOnDesc(user);
    }


    public void validateStake(BigDecimal stake) {
        if(stake == null || stake.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DomainException("Stake must be greater than 0");
        }
    }


    public void validateUserCanBet(User user, BigDecimal stake, UUID matchId) {

        if(user == null) {

            throw new DomainException("User not found");
        }

        if(user.getBalance().compareTo(stake) < 0) {
            System.out.println(">>> THROWING InsufficientBalanceException, balance="
                    + user.getBalance() + ", stake=" + stake);
            throw new InsufficientBalanceException("Not enough balance to place this bet", matchId);
        }

        if (!user.isActive()) {
            throw new DomainException("User is not active");
        }
    }


    public void validateMatchCanBeBetOn(Match match) {
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







    public BigDecimal resolveOdds(Match match, BetType betType) {
        return switch (betType) {
            case HOME_WIN -> match.getOddHome();
            case DRAW -> match.getOddDraw();
            case AWAY_WIN -> match.getOddAway();
        };
    }

    public BetType resolveBetType(int homeGoals, int awayGoals) {
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
            throw new DomainException("Match goals are missing. Cannot settle bets.");
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

//                userService.save(user);
            }else {
                bet.setBetStatus(BetStatus.LOST);
                bet.setSettledOn(now);
            }
//            betRepository.save(bet);

            }


        }

        public List<Bet> getBetsByMatchAndStatus(Match match, BetStatus betStatus) {
            return betRepository.findAllByMatchAndBetStatus(match, betStatus);
        }

    public void save(Bet bet) {
        betRepository.save(bet);
    }

    public long countByBetStatus(BetStatus betStatus) {
        return betRepository.countByBetStatus(betStatus);
    }


    public long countAllBets() {
        return betRepository.count();
    }
}


