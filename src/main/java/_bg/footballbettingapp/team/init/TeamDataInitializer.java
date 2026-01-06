package _bg.footballbettingapp.team.init;

import _bg.footballbettingapp.common.model.Country;
import _bg.footballbettingapp.team.model.Team;
import _bg.footballbettingapp.team.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TeamDataInitializer implements CommandLineRunner {

    private final TeamRepository teamRepository;
    @Autowired
    public TeamDataInitializer(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (teamRepository.count() > 0) {
            return;
        }


        List<Team> teams = List.of(
                Team.builder()
                        .name("Manchester City")
                        .shortName("MCI")
                        .country(Country.UNITED_KINGDOM)   // каквото имаш в enum-а
                        .logoUrl(null)              // може да добавиш по-късно
                        .build(),
                Team.builder()
                        .name("Liverpool")
                        .shortName("LIV")
                        .country(Country.UNITED_KINGDOM)
                        .logoUrl(null)
                        .build(),
                Team.builder()
                        .name("Real Madrid")
                        .shortName("RMA")
                        .country(Country.SPAIN)
                        .logoUrl(null)
                        .build(),
                Team.builder()
                        .name("Barcelona")
                        .shortName("BAR")
                        .country(Country.SPAIN)
                        .logoUrl(null)
                        .build(),
                Team.builder()
                        .name("Atletico Madrid")
                        .shortName("ATM")
                        .country(Country.SPAIN)
                        .logoUrl(null)
                        .build(),
                Team.builder()
                        .name("Juventus")
                        .shortName("JUV")
                        .country(Country.ITALY)
                        .logoUrl(null)
                        .build(),
                Team.builder()
                        .name("Chelsea")
                        .shortName("CHE")
                        .country(Country.UNITED_KINGDOM)
                        .logoUrl(null)
                        .build(),
                Team.builder()
                        .name("Arsenal")
                        .shortName("ARS")
                        .country(Country.UNITED_KINGDOM)
                        .logoUrl(null)
                        .build(),
                Team.builder()
                        .name("Bayern Munich")
                        .shortName("BAY")
                        .country(Country.GERMANY)
                        .logoUrl(null)
                        .build(),
                Team.builder()
                        .name("Borussia Dortmund")
                        .shortName("BUD")
                        .country(Country.GERMANY)
                        .logoUrl(null)
                        .build(),
                Team.builder()
                        .name("Manchester United")
                        .shortName("MUN")
                        .country(Country.UNITED_KINGDOM)
                        .logoUrl(null)
                        .build(),
                Team.builder()
                        .name("Tottenham Hotspur")
                        .shortName("TOT")
                        .country(Country.UNITED_KINGDOM)
                        .logoUrl(null)
                        .build(),
                Team.builder()
                        .name("Inter Milan")
                        .shortName("INT")
                        .country(Country.ITALY)
                        .logoUrl(null)
                        .build(),
                Team.builder()
                        .name("Newcastle")
                        .shortName("NEW")
                        .country(Country.UNITED_KINGDOM)
                        .logoUrl(null)
                        .build(),
                Team.builder()
                        .name("Bologna")
                        .shortName("BOL")
                        .country(Country.ITALY)
                        .logoUrl(null)
                        .build(),
                Team.builder()
                        .name("CSKA-Sofia")
                        .shortName("CSKA")
                        .country(Country.BULGARIA)
                        .logoUrl(null)
                        .build(),
                Team.builder()
                        .name("Levski-Sofia")
                        .shortName("LSK")
                        .country(Country.BULGARIA)
                        .logoUrl(null)
                        .build()

                // ... добавяш още
        );

        teamRepository.saveAll(teams);
    }
}
