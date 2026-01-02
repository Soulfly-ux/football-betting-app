package _bg.footballbettingapp.match.service;

import _bg.footballbettingapp.match.model.Match;
import _bg.footballbettingapp.match.model.MatchStatus;
import _bg.footballbettingapp.match.repository.MatchRepository;
import _bg.footballbettingapp.team.model.Team;
import _bg.footballbettingapp.team.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
public class MatchInit implements CommandLineRunner {


    private final MatchService matchService;
    private final MatchRepository matchRepository;
    private final TeamService teamService;
    @Autowired
    public MatchInit(MatchService matchService, MatchRepository matchRepository, TeamService teamService) {
        this.matchService = matchService;
        this.matchRepository = matchRepository;
        this.teamService = teamService;
    }

    @Override
    public void run(String... args) throws Exception {




        Team arsenal = teamService.getByName("Arsenal");
        Team chelsea = teamService.getByName("Chelsea");
        Team barca   = teamService.getByName("Barcelona");
        Team real    = teamService.getByName("Real Madrid");
        Team inter    = teamService.getByName("Inter Milan");
        Team atleticoMadrid = teamService.getByName("Atletico Madrid");
        Team juventus    = teamService.getByName("Juventus");
        Team manchesterCity    = teamService.getByName("Manchester City");
        Team liverpool = teamService.getByName("Liverpool");
        Team tottenham = teamService.getByName(" Tottenham Hotspur");




        LocalDateTime base = LocalDateTime.of(LocalDate.now(), LocalTime.of(12, 0));

        Match match1 = new Match();
        match1.setHomeTeam(arsenal);
        match1.setAwayTeam(chelsea);
        match1.setLeagueName("Premier League");
        match1.setMatchStatus(MatchStatus.SCHEDULED);
        match1.setStartTime(base.plusDays(1));
        match1.setOddHome(new BigDecimal("2.20"));
        match1.setOddDraw(new BigDecimal("3.50"));
        match1.setOddAway(new BigDecimal("2.50"));


        Match match2 = new Match();
        match2.setHomeTeam(inter);
        match2.setAwayTeam(atleticoMadrid);
        match2.setLeagueName("Champions League");
        match2.setMatchStatus(MatchStatus.SCHEDULED);
        match2.setStartTime(base.plusDays(1));
        match2.setOddHome(new BigDecimal("2.10"));
        match2.setOddDraw(new BigDecimal("2.50"));
        match2.setOddAway(new BigDecimal("2.20"));

        Match match3 = new Match();
        match3.setHomeTeam(barca);
        match3.setAwayTeam(real);
        match3.setLeagueName("La Liga");
        match3.setMatchStatus(MatchStatus.SCHEDULED);
        match3.setStartTime(base.plusDays(1));
        match3.setOddHome(new BigDecimal("2.00"));
        match3.setOddDraw(new BigDecimal("3.00"));
        match3.setOddAway(new BigDecimal("2.60"));


        Match match4 = new Match();
        match4.setHomeTeam(manchesterCity);
        match4.setAwayTeam(juventus);
        match4.setLeagueName("Champions League");
        match4.setMatchStatus(MatchStatus.SCHEDULED);
        match4.setStartTime(base.minusDays(3));
        match4.setOddHome(new BigDecimal("2.10"));
        match4.setOddDraw(new BigDecimal("3.50"));
        match4.setOddAway(new BigDecimal("2.50"));


        Match match5 = Match.builder()
                .homeTeam(liverpool)
                .awayTeam(tottenham)
                .leagueName("Premier League")
                .matchStatus(MatchStatus.SCHEDULED)
                .startTime(base.minusDays(5))
                .oddHome(new BigDecimal("1.90"))
                .oddDraw(new BigDecimal("2.20"))
                .oddAway(new BigDecimal("3.00"))
                .build();





       if (matchRepository.count() > 0){
           return;
       }




       seedMatch(match1);
       seedMatch(match2);
       seedMatch(match3);
       seedMatch(match4);
       seedMatch(match5);



    }


    private void seedMatch(Match match) {

        boolean existed = matchRepository.existsByHomeTeamAndAwayTeamAndStartTime(
                match.getHomeTeam(),
                match.getAwayTeam(),
                match.getStartTime());

        if (!existed) {
            matchService.save(match);
            System.out.println("Seeded match: "
                    + match.getHomeTeam().getName() + " vs "
                    + match.getAwayTeam().getName() + " at "
                    + match.getStartTime());
        } else {
            System.out.println("Skipped existing match: "
                    + match.getHomeTeam().getName() + " vs "
                    + match.getAwayTeam().getName() + " at "
                    + match.getStartTime());
        }
    }
}
