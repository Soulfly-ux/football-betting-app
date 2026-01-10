package _bg.footballbettingapp.user.service;


import _bg.footballbettingapp.bet.model.BetStatus;
import _bg.footballbettingapp.bet.service.BetService;
import _bg.footballbettingapp.match.model.MatchStatus;
import _bg.footballbettingapp.match.service.MatchAdminService;
import _bg.footballbettingapp.web.dto.AdminDashboardStats;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
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
        AdminDashboardStats stats = new AdminDashboardStats();


        stats.setTotalUsers(userAdminService.countUsers());
        stats.setAdminUsers(userAdminService.countAdmins());
        stats.setActiveUsers(userAdminService.countActiveUsers());



        stats.setTotalBets(betService.countAllBets());
        stats.setPendingBets(betService.countByBetStatus(BetStatus.PENDING));
        stats.setWonBets(betService.countByBetStatus(BetStatus.WON));
        stats.setLostBets(betService.countByBetStatus(BetStatus.LOST));
        stats.setCancelledBets(betService.countByBetStatus(BetStatus.CANCELLED));


        stats.setTotalMatches(matchAdminService.countAll());
        stats.setInProgressMatches(matchAdminService.countByStatus(MatchStatus.IN_PROGRESS));
        stats.setFinishedMatches(matchAdminService.countByStatus(MatchStatus.FINISHED));
        stats.setCancelledMatches(matchAdminService.countByStatus(MatchStatus.CANCELLED));
        stats.setScheduledMatches(matchAdminService.countByStatus(MatchStatus.SCHEDULED));




        return stats;
    }
}
