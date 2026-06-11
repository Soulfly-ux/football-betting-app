package _bg.footballbettingapp.bet;

import _bg.footballbettingapp.bet.model.Bet;
import _bg.footballbettingapp.bet.model.BetStatus;
import _bg.footballbettingapp.bet.model.BetType;
import _bg.footballbettingapp.bet.repository.BetRepository;
import _bg.footballbettingapp.bet.service.BetService;
import _bg.footballbettingapp.email.service.NotificationService;
import _bg.footballbettingapp.exception.DomainException;
import _bg.footballbettingapp.exception.InsufficientBalanceException;
import _bg.footballbettingapp.match.model.Match;
import _bg.footballbettingapp.match.model.MatchStatus;
import _bg.footballbettingapp.match.service.MatchService;
import _bg.footballbettingapp.team.model.Team;
import _bg.footballbettingapp.user.model.User;
import _bg.footballbettingapp.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class BetServiceUTests {
    @Mock
    private  BetRepository betRepository;

    @Mock
    private  MatchService matchService;

    @Mock
    private  UserService userService;

    @Mock
    private  NotificationService notificationService;

    @InjectMocks
    private BetService betService;


    @Test
    void givenNullStake_whenValidateStake_thenExceptionIsThrown() {
        // Given
        BigDecimal stake = null;

        // When & Then
        assertThrows(DomainException.class, () -> betService.validateStake(stake));


    }

    @Test
    void givenZeroStake_whenValidateStake_thenExceptionIsThrown() {
        // Given
        BigDecimal stake = BigDecimal.ZERO;

        // When & Then
        assertThrows(DomainException.class, () -> betService.validateStake(stake));


    }

    @Test
    void givenNegativeStake_whenValidateStake_thenExceptionIsThrown() {

        // Given
        BigDecimal stake = BigDecimal.valueOf(-5);

        // When & Then
        assertThrows(DomainException.class, () -> betService.validateStake(stake));


    }

    @Test
    void givenPositiveStake_whenValidateStake_thenNoExceptionIsThrown() {

        // Given
        BigDecimal stake = BigDecimal.valueOf(100);

        // When & Then
        assertDoesNotThrow(() -> betService.validateStake(stake));

    }

    @Test
    void givenNullUser_whenValidateUserCanBet_thenExceptionIsThrown() {

        // Given
        User user = null;
        BigDecimal stake = BigDecimal.valueOf(12);
        UUID matchId = UUID.randomUUID();


        // When & Then
        assertThrows(DomainException.class, () -> betService.validateUserCanBet(user, stake, matchId));



    }

    @Test
    void givenUserWithInsufficientBalance_whenValidateUserCanBet_thenExceptionIsThrown() {
        // Given
        User user = User.builder()
                .isActive(true)
                .balance(BigDecimal.valueOf(0))
                .build();
        BigDecimal stake = BigDecimal.valueOf(10);
        UUID matchId = UUID.randomUUID();

        // When & Then
        assertThrows(InsufficientBalanceException.class, () -> betService.validateUserCanBet(user, stake, matchId));

    }

    @Test
    void givenInactiveUser_whenValidateUserCanBet_thenExceptionIsThrown() {

        // Given
        User user = User.builder()
                .isActive(false)
                .balance(BigDecimal.valueOf(1000))
                .build();
        BigDecimal stake = BigDecimal.valueOf(10);
        UUID matchId = UUID.randomUUID();

        // When & Then
        assertThrows(DomainException.class, () -> betService.validateUserCanBet(user, stake, matchId));


    }

    @Test
    void givenActiveUserWithEnoughBalance_whenValidateUserCanBet_thenNoExceptionIsThrown() {

        // Given
        User user = User.builder()
                .id(UUID.randomUUID())
                .balance(BigDecimal.valueOf(1000))
                .isActive(true)
                .build();
        BigDecimal stake = BigDecimal.valueOf(10);
        UUID matchId = UUID.randomUUID();

        // When & Then
        assertDoesNotThrow(() -> betService.validateUserCanBet(user, stake, matchId));


    }

    @Test
    void givenNullMatch_whenValidateMatchCanBeBetOn_thenExceptionIsThrown() {

        // Given
        Match match = null;

        // When & Then
        assertThrows(DomainException.class, () -> betService.validateMatchCanBeBetOn(match));
    }

    @Test
    void givenMatchThatIsNotScheduled_whenValidateMatchCanBeBetOn_thenExceptionIsThrown() {
        // Given
        Match match = Match.builder()
                .matchStatus(MatchStatus.CANCELLED)
                .build();

        // When & Then
        assertThrows(DomainException.class, () -> betService.validateMatchCanBeBetOn(match));

    }



    @Test
    void givenStartedMatch_whenValidateMatchCanBeBetOn_thenExceptionIsThrown() {
        // Given
        Match match = Match.builder()
                .matchStatus(MatchStatus.SCHEDULED)
                .startTime(LocalDateTime.now().minusDays(1))
                .build();

        // When & Then
        assertThrows(DomainException.class, () -> betService.validateMatchCanBeBetOn(match));
    }

    @Test
    void givenMatchWithMissingOdds_whenValidateMatchCanBeBetOn_thenExceptionIsThrown() {

        Match match = Match.builder()
                .matchStatus(MatchStatus.SCHEDULED)
                .startTime(LocalDateTime.now().plusDays(1))
                .oddHome(BigDecimal.valueOf(2.5))
                .oddAway(BigDecimal.valueOf(3.0))
                .build();

        // When & Then
        assertThrows(DomainException.class, () -> betService.validateMatchCanBeBetOn(match));

    }

    @Test
    void givenScheduledFutureMatchWithOdds_whenValidateMatchCanBeBetOn_thenNoExceptionIsThrown() {

        // Given
        Match match = Match.builder()
                .matchStatus(MatchStatus.SCHEDULED)
                .startTime(LocalDateTime.now().plusDays(1))
                .oddHome(BigDecimal.valueOf(2.5))
                .oddDraw(BigDecimal.valueOf(4.0))
                .oddAway(BigDecimal.valueOf(3.0))
                .build();


        // When & Then
        assertDoesNotThrow(() -> betService.validateMatchCanBeBetOn(match));

    }


    @Test
    void givenHomeWinBetType_whenResolveOdds_thenReturnHomeOdds() {
        // Given
        Match match = Match.builder()
                .oddHome(BigDecimal.valueOf(2.5))
                .oddDraw(BigDecimal.valueOf(4.0))
                .oddAway(BigDecimal.valueOf(3.0))
                .build();

        BetType betType = BetType.HOME_WIN;

        // When
        BigDecimal result = betService.resolveOdds(match, betType);

        // Then
        assertEquals( match.getOddHome(), result);
    }

    @Test
    void givenDrawBetType_whenResolveOdds_thenReturnDrawOdds() {
        // Given
        Match match = Match.builder()
                .oddHome(BigDecimal.valueOf(2.5))
                .oddDraw(BigDecimal.valueOf(4.0))
                .oddAway(BigDecimal.valueOf(3.0))
                .build();

        BetType betType = BetType.DRAW;

        // When
        BigDecimal result = betService.resolveOdds(match, betType);

        // Then
        assertEquals( match.getOddDraw(), result);

    }

    @Test
    void givenAwayWinBetType_whenResolveOdds_thenReturnAwayOdds() {

        // Given
        Match match = Match.builder()
                .oddHome(BigDecimal.valueOf(2.5))
                .oddDraw(BigDecimal.valueOf(4.0))
                .oddAway(BigDecimal.valueOf(3.0))
                .build();

        BetType betType = BetType.AWAY_WIN;

        // When
        BigDecimal result = betService.resolveOdds(match, betType);

        // Then
        assertEquals( match.getOddAway(), result);

    }


       @Test
       void givenHomeGoalsGreaterThanAwayGoals_whenResolveBetType_thenReturnHomeWin() {
        // Given
        int homeGoals = 3;
        int awayGoals = 1;

        BetType betType = BetType.HOME_WIN;


        // When
           BetType resolveBetType = betService.resolveBetType(homeGoals, awayGoals);

           // Then
           assertEquals(betType, resolveBetType);


       }


    @Test
    void givenAwayGoalsGreaterThanHomeGoals_whenResolveBetType_thenReturnAwayWin() {
        // Given
        int homeGoals = 1;
        int awayGoals = 5;

        // When
        BetType resolveBetType = betService.resolveBetType(homeGoals, awayGoals);

        // Then
        assertEquals(BetType.AWAY_WIN, resolveBetType);


    }


    @Test
    void givenEqualGoals_whenResolveBetType_thenReturnDraw() {
        // Given
        int homeGoals = 0;
        int awayGoals = 0;

        // When
        BetType resolveBetType = betService.resolveBetType(homeGoals, awayGoals);

        // Then
        assertEquals(BetType.DRAW, resolveBetType);


    }

    @Test
    void givenMissingUser_whenPlaceBet_thenExceptionIsThrown() {
        // Given
        UUID userId = UUID.randomUUID();
        UUID matchId = UUID.randomUUID();
        BetType betType = BetType.HOME_WIN;
        BigDecimal stake = BigDecimal.valueOf(3);

        when(userService.getUserById(userId)).thenThrow(new DomainException("Missing user"));

        // When & Then
        assertThrows(DomainException.class, () -> betService.placeBet(userId, matchId, betType, stake));
        verify(matchService, never()).getMatchById(matchId);
        verify(betRepository, never()).save(any());
        verify(notificationService, never()).createNotification(any(),any(), any());
        verify(userService, never()).save(any());

    }

    @Test
    void givenMissingMatch_whenPlaceBet_thenExceptionIsThrown() {
        // Given
        UUID userId = UUID.randomUUID();
        UUID matchId = UUID.randomUUID();
        BetType betType = BetType.HOME_WIN;
        BigDecimal stake = BigDecimal.valueOf(3);

        User user = User.builder().build();

        when(userService.getUserById(userId)).thenReturn(user);
        when(matchService.getMatchById(matchId)).thenThrow(new DomainException("Match not found"));

        // When & Then
        assertThrows(DomainException.class, () -> betService.placeBet(userId,matchId,betType,stake));
        verify(betRepository, never()).save(any());
        verify(notificationService, never()).createNotification(any(),any(), any());
        verify(userService, never()).save(any());


    }

    @Test
    void givenInvalidStake_whenPlaceBet_thenExceptionIsThrown() {

        // Given
        UUID userId = UUID.randomUUID();
        UUID matchId = UUID.randomUUID();
        BetType betType = BetType.HOME_WIN;
        BigDecimal stake = BigDecimal.ZERO;

        User user = User.builder().build();
        Match match = Match.builder().build();

        when(userService.getUserById(userId)).thenReturn(user);
        when(matchService.getMatchById(matchId)).thenReturn(match);



        // When & Then
        assertThrows(DomainException.class, () -> betService.placeBet(userId,matchId,betType,stake));
        verify(betRepository, never()).save(any());
        verify(notificationService, never()).createNotification(any(),any(), any());
        verify(userService, never()).save(any());



    }

    @Test
    void givenUserWithInsufficientBalance_whenPlaceBet_thenExceptionIsThrown(){

        // Given
        UUID userId = UUID.randomUUID();
        UUID matchId = UUID.randomUUID();
        BetType betType = BetType.HOME_WIN;

        User user = User.builder()
                .balance(BigDecimal.valueOf(1))
                .isActive(true)
                .build();

        Match match = Match.builder().build();

        BigDecimal stake = BigDecimal.valueOf(10);

        when(userService.getUserById(userId)).thenReturn(user);
        when(matchService.getMatchById(matchId)).thenReturn(match);


        // When & Then
        assertThrows(InsufficientBalanceException.class, () -> betService.placeBet(userId, matchId, betType, stake));
        verify(betRepository, never()).save(any());
        verify(notificationService, never()).createNotification(any(),any(), any());
        verify(userService, never()).save(any());


    }
    @Test
    void givenInactiveUser_whenPlaceBet_thenExceptionIsThrown() {

        // Given
        UUID userId = UUID.randomUUID();
        UUID matchId = UUID.randomUUID();
        BetType betType = BetType.HOME_WIN;

        User user = User.builder()
                .balance(BigDecimal.valueOf(100))
                .isActive(false)
                .build();

        Match match = Match.builder().build();

        BigDecimal stake = BigDecimal.valueOf(10);

        when(userService.getUserById(userId)).thenReturn(user);
        when(matchService.getMatchById(matchId)).thenReturn(match);

        // When & Then
        assertThrows(DomainException.class, () -> betService.placeBet(userId, matchId, betType, stake));
        verify(betRepository, never()).save(any());
        verify(notificationService, never()).createNotification(any(),any(), any());
        verify(userService, never()).save(any());


    }

    @Test
    void givenMatchThatIsNotScheduled_whenPlaceBet_thenExceptionIsThrown() {

        // Given
        UUID userId = UUID.randomUUID();
        UUID matchId = UUID.randomUUID();
        BetType betType = BetType.HOME_WIN;

        User user = User.builder()
                .balance(BigDecimal.valueOf(100))
                .isActive(true)
                .build();

        Match match = Match.builder()
                .matchStatus(MatchStatus.CANCELLED)
                .build();

        BigDecimal stake = BigDecimal.valueOf(10);

        when(userService.getUserById(userId)).thenReturn(user);
        when(matchService.getMatchById(matchId)).thenReturn(match);

        // When & Then
        assertThrows(DomainException.class, () -> betService.placeBet(userId, matchId, betType, stake));
        verify(betRepository, never()).save(any());
        verify(notificationService, never()).createNotification(any(),any(), any());
        verify(userService, never()).save(any());


    }
    @Test
    void givenStartedMatch_whenPlaceBet_thenExceptionIsThrown() {

        // Given
        UUID userId = UUID.randomUUID();
        UUID matchId = UUID.randomUUID();
        BetType betType = BetType.HOME_WIN;

        User user = User.builder()
                .balance(BigDecimal.valueOf(100))
                .isActive(true)
                .build();

        Match match = Match.builder()
                .matchStatus(MatchStatus.SCHEDULED)
                .startTime(LocalDateTime.now().minusDays(1))
                .build();

        BigDecimal stake = BigDecimal.valueOf(10);

        when(userService.getUserById(userId)).thenReturn(user);
        when(matchService.getMatchById(matchId)).thenReturn(match);

        // When & Then
        assertThrows(DomainException.class, () -> betService.placeBet(userId, matchId, betType, stake));
        verify(betRepository, never()).save(any());
        verify(notificationService, never()).createNotification(any(),any(), any());
        verify(userService, never()).save(any());

    }

    @Test
    void givenMatchWithMissingOdds_whenPlaceBet_thenExceptionIsThrown() {

        // Given
        UUID userId = UUID.randomUUID();
        UUID matchId = UUID.randomUUID();
        BetType betType = BetType.HOME_WIN;

        User user = User.builder()
                .balance(BigDecimal.valueOf(100))
                .isActive(true)
                .build();

        Match match = Match.builder()
                .matchStatus(MatchStatus.SCHEDULED)
                .startTime(LocalDateTime.now().plusDays(2))
                .oddDraw(BigDecimal.valueOf(3.00))
                .oddAway(BigDecimal.valueOf(3.90))
                .build();

        BigDecimal stake = BigDecimal.valueOf(10);

        when(userService.getUserById(userId)).thenReturn(user);
        when(matchService.getMatchById(matchId)).thenReturn(match);

        // When & Then
        assertThrows(DomainException.class, () -> betService.placeBet(userId, matchId, betType, stake));
        verify(betRepository, never()).save(any());
        verify(notificationService, never()).createNotification(any(),any(), any());
        verify(userService, never()).save(any());


    }

    @Test
    void givenValidBetData_whenPlaceBet_thenBetIsPlacedSuccessfully() {
        // Given
        UUID userId = UUID.randomUUID();
        UUID matchId = UUID.randomUUID();
        BetType betType = BetType.HOME_WIN;

        User user = User.builder()
                .id(userId)
                .balance(BigDecimal.valueOf(100))
                .isActive(true)
                .build();

        Team homeTeam = Team.builder()
                .name("Inter Milan")
                .build();
        Team awayTeam = Team.builder()
                .name("Juventus")
                .build();

        Match match = Match.builder()
                .id(matchId)
                .matchStatus(MatchStatus.SCHEDULED)
                .startTime(LocalDateTime.now().plusDays(2))
                .homeTeam(homeTeam)
                .awayTeam(awayTeam)
                .oddHome(BigDecimal.valueOf(2.00))
                .oddDraw(BigDecimal.valueOf(3.00))
                .oddAway(BigDecimal.valueOf(3.90))
                .build();

        BigDecimal stake = BigDecimal.valueOf(10);

        Bet savedBet = Bet.builder()
                .id(UUID.randomUUID())
                .betType(betType)
                .stake(stake)
                .user(user)
                .match(match)
                .potentialWinningAmount(BigDecimal.valueOf(20))
                .betStatus(BetStatus.PENDING)
                .odds(BigDecimal.valueOf(2))
                .build();

        when(userService.getUserById(userId)).thenReturn(user);
        when(matchService.getMatchById(matchId)).thenReturn(match);
        when(betRepository.save(any())).thenReturn(savedBet);



        // When
        Bet result = betService.placeBet(userId, matchId, betType, stake);

        // Then
        assertEquals(savedBet.getId(), result.getId());
        assertEquals(savedBet.getBetType(), result.getBetType());
        assertEquals(savedBet.getStake(), result.getStake());
        assertEquals(savedBet.getBetStatus(), result.getBetStatus());
        assertEquals(savedBet.getOdds(), result.getOdds());
        assertEquals(savedBet.getPotentialWinningAmount(), result.getPotentialWinningAmount());
        assertEquals( BigDecimal.valueOf(90), user.getBalance());


        verify(betRepository).save(any());
        verify(notificationService).createNotification(any(),any(), any());
        verify(userService).save(user);


    }

    @Test
   void givenMatchThatIsNotFinished_whenSettleBetForMatch_thenExceptionIsThrown() {
        // Given
        UUID matchId = UUID.randomUUID();
        Match match = Match.builder()
                .id(matchId)
                .matchStatus(MatchStatus.SCHEDULED)
                .build();

        when(matchService.getMatchById(matchId)).thenReturn(match);

        // When & Then
        assertThrows(DomainException.class, () -> betService.settleBetForMatch(matchId));
        verify(betRepository, never()).findAllByMatchAndBetStatus(match, BetStatus.PENDING);

    }

    @Test
    void givenFinishedMatchWithMissingGoals_whenSettleBetForMatch_thenExceptionIsThrown() {

        // Given
        UUID matchId = UUID.randomUUID();

        Match match = Match.builder()
                .id(matchId)
                .matchStatus(MatchStatus.FINISHED)
                .homeGoals(3)
                .build();

        when(matchService.getMatchById(matchId)).thenReturn(match);

        // When & Then
        assertThrows(DomainException.class, () -> betService.settleBetForMatch(matchId));
        verify(betRepository, never()).findAllByMatchAndBetStatus(match, BetStatus.PENDING);

    }

    @Test
    void givenFinishedMatchWithWinningPendingBet_whenSettleBetForMatch_thenBetIsWonAndUserBalanceIsIncreased() {
        // Given
        UUID matchId = UUID.randomUUID();


        Match match = Match.builder()
                .id(matchId)
                .matchStatus(MatchStatus.FINISHED)
                .homeGoals(3)
                .awayGoals(1)
                .build();

        User user = User.builder()
                .balance(BigDecimal.valueOf(100))
                .build();

        Bet bet = Bet.builder()
                .betStatus(BetStatus.PENDING)
                .betType(BetType.HOME_WIN)
                .user(user)
                .potentialWinningAmount(BigDecimal.valueOf(30))
                .build();

        List<Bet> pendingBets = List.of(bet);

        when(matchService.getMatchById(matchId)).thenReturn(match);
        when(betRepository.findAllByMatchAndBetStatus(match, BetStatus.PENDING)).thenReturn(pendingBets);

        // When
        betService.settleBetForMatch(matchId);

        // Then
        assertEquals(BetStatus.WON, bet.getBetStatus()) ;
        assertEquals(BigDecimal.valueOf(130), user.getBalance());
        assertNotNull(bet.getSettledOn());
        verify(betRepository).findAllByMatchAndBetStatus(match, BetStatus.PENDING);


    }

    @Test
    void givenFinishedMatchWithLosingPendingBet_whenSettleBetForMatch_thenBetIsLostAndBalanceIsNotChanged() {

        // Given
        UUID matchId = UUID.randomUUID();


        Match match = Match.builder()
                .id(matchId)
                .matchStatus(MatchStatus.FINISHED)
                .homeGoals(0)
                .awayGoals(1)
                .build();

        User user = User.builder()
                .balance(BigDecimal.valueOf(100))
                .build();

        Bet bet = Bet.builder()
                .betStatus(BetStatus.PENDING)
                .betType(BetType.HOME_WIN)
                .potentialWinningAmount(BigDecimal.valueOf(20))
                .user(user)
                .build();



        List<Bet> pendingBets = List.of(bet);

        when(matchService.getMatchById(matchId)).thenReturn(match);
        when(betRepository.findAllByMatchAndBetStatus(match, BetStatus.PENDING)).thenReturn(pendingBets);

        // When
        betService.settleBetForMatch(matchId);

        // Then
        assertEquals(BetStatus.LOST, bet.getBetStatus()) ;

        assertNotNull(bet.getSettledOn());
        verify(betRepository).findAllByMatchAndBetStatus(match, BetStatus.PENDING);


    }

    @Test
    void getBetsByUser() {
        // Given
        User user = User.builder().build();

        List<Bet> bets = List.of(new Bet(), new Bet());
        when(betRepository.findAllByUserOrderByCreatedOnDesc(user)).thenReturn(bets);

        // When
        List<Bet> betsByUser = betService.getBetsByUser(user);

        // Then
        assertEquals(bets, betsByUser);
        verify(betRepository).findAllByUserOrderByCreatedOnDesc(user);

    }

    @Test
    void givenMatchAndBetStatus_whenGetBetsByMatchAndStatus_thenReturnBets() {}


}
