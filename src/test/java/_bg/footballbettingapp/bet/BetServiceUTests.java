package _bg.footballbettingapp.bet;

import _bg.footballbettingapp.bet.repository.BetRepository;
import _bg.footballbettingapp.bet.service.BetService;
import _bg.footballbettingapp.email.service.NotificationService;
import _bg.footballbettingapp.exception.DomainException;
import _bg.footballbettingapp.exception.InsufficientBalanceException;
import _bg.footballbettingapp.match.service.MatchService;
import _bg.footballbettingapp.user.model.User;
import _bg.footballbettingapp.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
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



}
