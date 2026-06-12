package _bg.footballbettingapp.match;

import _bg.footballbettingapp.exception.DomainException;
import _bg.footballbettingapp.match.model.Match;
import _bg.footballbettingapp.match.repository.MatchRepository;
import _bg.footballbettingapp.match.service.MatchService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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


}
