package _bg.footballbettingapp.match;

import _bg.footballbettingapp.bet.service.BetService;
import _bg.footballbettingapp.match.model.Match;
import _bg.footballbettingapp.match.model.MatchStatus;
import _bg.footballbettingapp.match.repository.MatchRepository;
import _bg.footballbettingapp.match.service.MatchAdminService;
import _bg.footballbettingapp.team.service.TeamService;
import _bg.footballbettingapp.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
}
