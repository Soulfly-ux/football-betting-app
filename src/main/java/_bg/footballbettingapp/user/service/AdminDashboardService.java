package _bg.footballbettingapp.user.service;


import _bg.footballbettingapp.bet.model.BetStatus;
import _bg.footballbettingapp.bet.service.BetService;
import _bg.footballbettingapp.match.model.MatchStatus;
import _bg.footballbettingapp.match.service.MatchAdminService;
import _bg.footballbettingapp.web.dto.AdminDashboardStats;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Builder
public class AdminDashboardService {


    private final UserAdminService userAdminService;
    private final BetService betService;
    private final MatchAdminService matchAdminService;

    public AdminDashboardService(UserAdminService userAdminService, BetService betService, MatchAdminService matchAdminService) {
        this.userAdminService = userAdminService;
        this.betService = betService;
        this.matchAdminService = matchAdminService;
    }


    public AdminDashboardStats getStats() {
//        AdminDashboardStats stats = new AdminDashboardStats();
//
//
//
//
//        stats.setTotalUsers(userAdminService.countUsers());
//        stats.setAdminUsers(userAdminService.countAdmins());
//        stats.setActiveUsers(userAdminService.countActiveUsers());
//
//
//
//        stats.setTotalBets(betService.countAllBets());
//        stats.setPendingBets(betService.countByBetStatus(BetStatus.PENDING));
//        stats.setWonBets(betService.countByBetStatus(BetStatus.WON));
//        stats.setLostBets(betService.countByBetStatus(BetStatus.LOST));
//        stats.setCancelledBets(betService.countByBetStatus(BetStatus.CANCELLED));
//
//
//        stats.setTotalMatches(matchAdminService.countAll());
//        stats.setInProgressMatches(matchAdminService.countByStatus(MatchStatus.IN_PROGRESS));
//        stats.setFinishedMatches(matchAdminService.countByStatus(MatchStatus.FINISHED));
//        stats.setCancelledMatches(matchAdminService.countByStatus(MatchStatus.CANCELLED));
//        stats.setScheduledMatches(matchAdminService.countByStatus(MatchStatus.SCHEDULED));
//
//         stats.setOverdueMatches(matchAdminService.countOverdueMatches());
//
//        return stats;

        return AdminDashboardStats.builder()
                                .totalUsers(userAdminService.countUsers())
                                .adminUsers(userAdminService.countAdmins())
                                .activeUsers(userAdminService.countActiveUsers())
                                .totalBets(betService.countAllBets())
                                .pendingBets(betService.countByBetStatus(BetStatus.PENDING))
                                .wonBets(betService.countByBetStatus(BetStatus.WON))
                                .lostBets(betService.countByBetStatus(BetStatus.LOST))
                                .cancelledBets(betService.countByBetStatus(BetStatus.CANCELLED))
                                .totalMatches(matchAdminService.countAll())
                                .inProgressMatches(matchAdminService.countByStatus(MatchStatus.IN_PROGRESS))
                                .finishedMatches(matchAdminService.countByStatus(MatchStatus.FINISHED))
                                .cancelledMatches(matchAdminService.countByStatus(MatchStatus.CANCELLED))
                                .scheduledMatches(matchAdminService.countByStatus(MatchStatus.SCHEDULED))
                                .overdueMatches(matchAdminService.countOverdueMatches())
                                .staleInProgressMatches(matchAdminService.countStaleInProgress(8))
                                .build();
    }
}










