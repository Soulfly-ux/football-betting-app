package _bg.footballbettingapp.schedulers;


import _bg.footballbettingapp.match.service.MatchAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MatchStatusScheduler {

    private final MatchAdminService matchAdminService;

    @Autowired
    public MatchStatusScheduler(MatchAdminService matchAdminService) {
        this.matchAdminService = matchAdminService;
    }

    @Scheduled(fixedRate = 60000)
    public void updateMatchStatus() {
        matchAdminService.markScheduledMatchesAsInProgress();
    }
}
