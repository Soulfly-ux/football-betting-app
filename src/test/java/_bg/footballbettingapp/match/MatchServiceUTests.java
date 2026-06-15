package _bg.footballbettingapp.match;

import _bg.footballbettingapp.exception.DomainException;
import _bg.footballbettingapp.match.model.Match;
import _bg.footballbettingapp.match.model.MatchStatus;
import _bg.footballbettingapp.match.repository.MatchRepository;
import _bg.footballbettingapp.match.service.MatchService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class MatchServiceUTests {

    @Mock
    private MatchRepository matchRepository;

    @InjectMocks
    private MatchService matchService;



    @Test
    void givenExistingMatch_whenGetMatchById_thenReturnMatch() {
        UUID matchId = UUID.randomUUID();

        Match match = Match.builder().build();
        when(matchRepository.findById(matchId)).thenReturn(Optional.of(match));

        Match result = matchService.getMatchById(matchId);

        assertEquals(match, result);
        verify(matchRepository).findById(matchId);
    }

    @Test
    void givenMissingMatch_whenGetMatchById_thenExceptionIsThrown() {
        UUID matchId = UUID.randomUUID();

        when(matchRepository.findById(matchId)).thenReturn(Optional.empty());

       assertThrows(DomainException.class, () -> matchService.getMatchById(matchId));


    }

    @Test
    void givenMatchWithMissingGoals_whenGetResultAsString_thenReturnNA() {

         Match match = Match.builder()
                 .id(UUID.randomUUID())
                 .homeGoals(2)
                 .build();

        String resultAsString = matchService.getResultAsString(match);

        assertEquals("N/A", resultAsString);

    }

    @Test
    void givenMatchWithGoals_whenGetResultAsString_thenReturnFormattedScore() {

        Match match = Match.builder()
                .id(UUID.randomUUID())
                .homeGoals(2)
                .awayGoals(1)
                .build();

        String resultAsString = matchService.getResultAsString(match);

        assertEquals("2:1", resultAsString);


    }

    @Test
    void givenMissingMatch_whenSetResult_thenExceptionIsThrown() {


        UUID matchId = UUID.randomUUID();
        int homeGoals = 1;
        int awayGoals = 0;
        Match match = Match.builder().build();

        when(matchRepository.findById(matchId)).thenReturn(Optional.empty());

        assertThrows(DomainException.class, () -> matchService.setResult(matchId, homeGoals, awayGoals));
        verify(matchRepository, never()).save(any());
    }


    @Test
    void givenCancelledMatch_whenSetResult_thenExceptionIsThrown() {

        UUID matchId = UUID.randomUUID();
        int homeGoals = 1;
        int awayGoals = 0;

        Match match = Match.builder()
                .matchStatus(MatchStatus.CANCELLED)
                .build();

        when(matchRepository.findById(matchId)).thenReturn(Optional.of(match));


        assertThrows(DomainException.class, () -> matchService.setResult(matchId, homeGoals, awayGoals));
        verify(matchRepository, never()).save(any());

    }

    @Test
    void givenFinishedMatch_whenSetResult_thenExceptionIsThrown() {

        UUID matchId = UUID.randomUUID();
        int homeGoals = 1;
        int awayGoals = 0;

        Match match = Match.builder()
                .matchStatus(MatchStatus.FINISHED)
                .build();

        when(matchRepository.findById(matchId)).thenReturn(Optional.of(match));


        assertThrows(DomainException.class, () -> matchService.setResult(matchId, homeGoals, awayGoals));
        verify(matchRepository, never()).save(any());


    }

    @Test
    void givenScheduledMatch_whenSetResult_thenUpdateGoalsStatusAndSaveMatch() {

        UUID matchId = UUID.randomUUID();
        int homeGoals = 1;
        int awayGoals = 0;

        Match match = Match.builder()
                .id(matchId)
                .matchStatus(MatchStatus.SCHEDULED)
                .build();

        when(matchRepository.findById(matchId)).thenReturn(Optional.of(match));
        when(matchRepository.save(match)).thenReturn(match);

        Match result = matchService.setResult(matchId, homeGoals, awayGoals);


        assertEquals(match.getHomeGoals(), result.getHomeGoals());
        assertEquals(match.getAwayGoals(), result.getAwayGoals());
        assertEquals(MatchStatus.FINISHED, result.getMatchStatus());
        verify(matchRepository).save(match);


    }

    @Test
    void whenGetUpcomingMatches_thenReturnScheduledUpcomingMatches() {



        List<Match> upcomingMatches = List.of(new Match(), new Match(), new Match());
        when(matchRepository.findAllByMatchStatusAndStartTimeAfterOrderByStartTimeAsc(eq(MatchStatus.SCHEDULED), any(LocalDateTime.class))).thenReturn(upcomingMatches);


        List<Match> result = matchService.getUpcomingMatches();

        assertEquals(upcomingMatches, result);
        verify(matchRepository).findAllByMatchStatusAndStartTimeAfterOrderByStartTimeAsc(eq(MatchStatus.SCHEDULED), any(LocalDateTime.class));


    }

    @Test
    void whenCountMatches_thenReturnCount() {
        long count = 5;

        when(matchRepository.count()).thenReturn(count);

        long result = matchService.countMatches();

        assertEquals(count, result);
        verify(matchRepository).count();


    }

    @Test
    void givenMatch_whenSave_thenRepositorySaveIsCalled() {

        Match match = Match.builder().build();

        matchService.save(match);

        verify(matchRepository).save(match);

    }




}
