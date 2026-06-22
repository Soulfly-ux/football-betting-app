package _bg.footballbettingapp.user;

import _bg.footballbettingapp.bet.model.BetStatus;
import _bg.footballbettingapp.bet.service.BetService;
import _bg.footballbettingapp.match.model.MatchStatus;
import _bg.footballbettingapp.match.service.MatchAdminService;
import _bg.footballbettingapp.user.service.AdminDashboardService;
import _bg.footballbettingapp.user.service.UserAdminService;
import _bg.footballbettingapp.web.dto.AdminDashboardStats;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdminDashboardServiceUTests {

    @Mock
    private UserAdminService userAdminService;

    @Mock
    private BetService betService;

    @Mock
    private MatchAdminService matchAdminService;

    @InjectMocks
    private AdminDashboardService adminDashboardService;



    @Test
    void whenGetStats_thenReturnAggregatedDashboardStats() {

        long users = 120;
        long admins = 3;
        long activeUsers = 115;

        long allBets = 2000;
        long pendingBets = 500;
        long winBets = 500;
        long lostBets = 500;
        long cancelledBets = 500;

        long allMatches = 1000;
        long matchInProgress = 500;
        long finishedMatches = 500;
        long cancelledMatches = 500;
        long scheduledMatches = 500;
        long overdueMatches = 100;
        long staleInProgress = 22;

        when(userAdminService.countActiveUsers()).thenReturn(activeUsers);
        when(userAdminService.countAdmins()).thenReturn(admins);
        when(userAdminService.countUsers()).thenReturn(users);

        when(betService.countAllBets()).thenReturn(allBets);
        when(betService.countByBetStatus(BetStatus.PENDING)).thenReturn(pendingBets);
        when(betService.countByBetStatus(BetStatus.WON)).thenReturn(winBets);
        when(betService.countByBetStatus(BetStatus.LOST)).thenReturn(lostBets);
        when(betService.countByBetStatus(BetStatus.CANCELLED)).thenReturn(cancelledBets);

        when(matchAdminService.countAll()).thenReturn(allMatches);
        when(matchAdminService.countByStatus(MatchStatus.IN_PROGRESS)).thenReturn(matchInProgress);
        when(matchAdminService.countByStatus(MatchStatus.FINISHED)).thenReturn(finishedMatches);
        when(matchAdminService.countByStatus(MatchStatus.CANCELLED)).thenReturn(cancelledMatches);
        when(matchAdminService.countByStatus(MatchStatus.SCHEDULED)).thenReturn(scheduledMatches);
        when(matchAdminService.countOverdueMatches()).thenReturn(overdueMatches);
        when(matchAdminService.countStaleInProgress(8)).thenReturn(staleInProgress);


        AdminDashboardStats result = adminDashboardService.getStats();

        assertEquals(users, result.getTotalUsers());
        assertEquals(admins, result.getAdminUsers());
        assertEquals(activeUsers, result.getActiveUsers());

        assertEquals(allBets, result.getTotalBets());
        assertEquals(pendingBets, result.getPendingBets());
        assertEquals(winBets, result.getWonBets());
        assertEquals(lostBets, result.getLostBets());
        assertEquals(cancelledBets, result.getCancelledBets());

        assertEquals(allMatches, result.getTotalMatches());
        assertEquals(matchInProgress, result.getInProgressMatches());
        assertEquals(finishedMatches, result.getFinishedMatches());
        assertEquals(cancelledMatches, result.getCancelledMatches());
        assertEquals(scheduledMatches, result.getScheduledMatches());
        assertEquals(overdueMatches, result.getOverdueMatches());
        assertEquals(staleInProgress, result.getStaleInProgressMatches());



    }
}
