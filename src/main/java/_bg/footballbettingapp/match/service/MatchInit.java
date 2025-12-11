package _bg.footballbettingapp.match.service;

import _bg.footballbettingapp.match.model.Match;
import _bg.footballbettingapp.match.model.MatchStatus;
import _bg.footballbettingapp.team.model.Team;
import _bg.footballbettingapp.team.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class MatchInit implements CommandLineRunner {


    private final MatchService matchService;
    private final TeamService teamService;
    @Autowired
    public MatchInit(MatchService matchService, TeamService teamService) {
        this.matchService = matchService;
        this.teamService = teamService;
    }

    @Override
    public void run(String... args) throws Exception {

        if (matchService.countMatches() > 0) {
             return;
        }


        Team arsenal = teamService.getByName("Arsenal");
        Team chelsea = teamService.getByName("Chelsea");
        Team barca   = teamService.getByName("Barcelona");
        Team real    = teamService.getByName("Real Madrid");
        Team inter    = teamService.getByName("Inter Milan");
        Team atleticoMadrid = teamService.getByName("Atletico Madrid");
        Team juventus    = teamService.getByName("Juventus");
        Team manchesterCity    = teamService.getByName("Manchester City");


        LocalDateTime now = LocalDateTime.now();


        Match match1 = new Match();
        match1.setHomeTeam(arsenal);
        match1.setAwayTeam(chelsea);
        match1.setLeagueName("Premier League");
        match1.setMatchStatus(MatchStatus.SCHEDULED);
        match1.setStartTime(now.plusDays(1));
        match1.setOddHome(new BigDecimal("2.20"));
        match1.setOddDraw(new BigDecimal("3.50"));
        match1.setOddAway(new BigDecimal("2.50"));


        Match match2 = new Match();
        match2.setHomeTeam(inter);
        match2.setAwayTeam(atleticoMadrid);
        match2.setLeagueName("Champions League");
        match2.setMatchStatus(MatchStatus.SCHEDULED);
        match2.setStartTime(now.plusDays(2));
        match2.setOddHome(new BigDecimal("2.10"));
        match2.setOddDraw(new BigDecimal("2.50"));
        match2.setOddAway(new BigDecimal("2.20"));

        Match match3 = new Match();
        match3.setHomeTeam(barca);
        match3.setAwayTeam(real);
        match3.setLeagueName("La Liga");
        match3.setMatchStatus(MatchStatus.SCHEDULED);
        match3.setStartTime(now.plusDays(1));
        match3.setOddHome(new BigDecimal("2.00"));
        match3.setOddDraw(new BigDecimal("3.00"));
        match3.setOddAway(new BigDecimal("2.60"));


        Match match4 = new Match();
        match4.setHomeTeam(manchesterCity);
        match4.setAwayTeam(juventus);
        match4.setLeagueName("Champions League");
        match4.setMatchStatus(MatchStatus.SCHEDULED);
        match4.setStartTime(now.plusDays(1));
        match4.setOddHome(new BigDecimal("2.10"));
        match4.setOddDraw(new BigDecimal("3.50"));
        match4.setOddAway(new BigDecimal("2.50"));








        matchService.save(match1);
        matchService.save(match2);
        matchService.save(match3);
        matchService.save(match4);



    }
}
