package _bg.footballbettingapp.bet.service;

import _bg.footballbettingapp.bet.model.Bet;
import _bg.footballbettingapp.bet.model.BetType;
import _bg.footballbettingapp.bet.repository.BetRepository;
import _bg.footballbettingapp.match.model.Match;
import _bg.footballbettingapp.match.service.MatchService;
import _bg.footballbettingapp.user.model.User;
import _bg.footballbettingapp.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    public Bet placeBet(UUID userId, UUID matchId, BetType betType, BigDecimal stake) {

        User user = userService.getUserById(userId);
        Match match = matchService.getMatchById(matchId);
//        Bet bet = Bet.builder()
//                .user(user)
//                .match(match)
//                .betType(betType)
//                .stake(stake)
//                .build();

        return null;
    }
}
