package _bg.footballbettingapp.match;

import _bg.footballbettingapp.bet.service.BetService;
import _bg.footballbettingapp.exception.DomainException;
import _bg.footballbettingapp.match.model.Match;
import _bg.footballbettingapp.match.model.MatchStatus;
import _bg.footballbettingapp.match.repository.MatchRepository;
import _bg.footballbettingapp.match.service.MatchAdminService;
import _bg.footballbettingapp.team.service.TeamService;
import _bg.footballbettingapp.user.service.UserService;
import _bg.footballbettingapp.web.dto.EditMatchRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MatchAdminServiceUTests {

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private BetService betService;

    @Mock
    private TeamService teamService;

    @Mock
    private UserService userService;

    @InjectMocks
    private MatchAdminService matchAdminService;



    @Test
    void whenGetAdminOpenMatches_thenReturnScheduledAndInProgressMatches() {

        Match match1 = Match.builder()
                .matchStatus(MatchStatus.SCHEDULED)
                .build();

        Match match2 = Match.builder()
                .matchStatus(MatchStatus.IN_PROGRESS)
                .build();


        List<Match> matches = List.of(match1, match2);
        List<MatchStatus> matchStatuses = List.of(MatchStatus.SCHEDULED,MatchStatus.IN_PROGRESS );

        when(matchRepository.findAllByMatchStatusInOrderByStartTimeAsc(matchStatuses)).thenReturn(matches);

        List<Match> result = matchAdminService.getAdminOpenMatches();

        assertEquals(matches, result);
        verify(matchRepository).findAllByMatchStatusInOrderByStartTimeAsc(matchStatuses);


    }

    @Test
    void whenGetSupportedLeagues_thenReturnSupportedLeagues() {

        List<String> leagues = List.of("Premier League","La Liga", "Bundesliga", "Serie A", "Ligue 1","Champions League","Europa League", "Bulgarian Efbet League");

        List<String> supportedLeagues = matchAdminService.getSupportedLeagues();

        assertEquals(leagues, supportedLeagues);
    }


    @Test
    void givenMatch_whenMapToEditMatchRequest_thenReturnMappedDto() {

        Match match = Match.builder()
                .startTime(LocalDateTime.now())
                .oddHome(BigDecimal.valueOf(2.10))
                .oddDraw(BigDecimal.valueOf(3.00))
                .oddAway(BigDecimal.valueOf(2.60))
                .build();

        EditMatchRequest result = matchAdminService.mapToEditMatchRequest(match);

        assertEquals(match.getStartTime(), result.getStartTime());
        assertEquals(match.getOddHome(), result.getOddHome());
        assertEquals(match.getOddDraw(), result.getOddDraw());
        assertEquals(match.getOddAway(), result.getOddAway());

    }


    @Test
    void givenMissingMatch_whenFinishMatch_thenExceptionIsThrown() {

        UUID id = UUID.randomUUID();
        int homeGoals = 3;
        int awayGoals = 0;

       when(matchRepository.findById(id)).thenReturn(Optional.empty());

       assertThrows(DomainException.class, () -> matchAdminService.finishMatch(id, homeGoals, awayGoals));
       verify(matchRepository, never()).save(any());
       verify(betService, never()).settleBetForMatch(any());

    }


    @Test
    void givenFinishedMatch_whenFinishMatch_thenExceptionIsThrown() {

        UUID id = UUID.randomUUID();
        int homeGoals = 3;
        int awayGoals = 0;

        Match match = Match.builder()
                .matchStatus(MatchStatus.FINISHED)
                .build();

        when(matchRepository.findById(id)).thenReturn(Optional.of(match));

        assertThrows(DomainException.class, () -> matchAdminService.finishMatch(id, homeGoals, awayGoals));
        verify(matchRepository, never()).save(any());
        verify(betService, never()).settleBetForMatch(any());

    }

    @Test
    void givenCancelledMatch_whenFinishMatch_thenExceptionIsThrown() {
        UUID id = UUID.randomUUID();
        int homeGoals = 3;
        int awayGoals = 0;

        Match match = Match.builder()
                .matchStatus(MatchStatus.CANCELLED)
                .build();

        when(matchRepository.findById(id)).thenReturn(Optional.of(match));

        assertThrows(DomainException.class, () -> matchAdminService.finishMatch(id, homeGoals, awayGoals));
        verify(matchRepository, never()).save(any());
        verify(betService, never()).settleBetForMatch(any());

    }

    @Test
    void givenMatchNotInProgress_whenFinishMatch_thenExceptionIsThrown() {

        UUID id = UUID.randomUUID();
        int homeGoals = 3;
        int awayGoals = 0;

        Match match = Match.builder()
                .matchStatus(MatchStatus.SCHEDULED)
                .build();

        when(matchRepository.findById(id)).thenReturn(Optional.of(match));

        assertThrows(DomainException.class, () -> matchAdminService.finishMatch(id, homeGoals, awayGoals));
        verify(matchRepository, never()).save(any());
        verify(betService, never()).settleBetForMatch(any());

    }
}
