package _bg.footballbettingapp.team.service;

import _bg.footballbettingapp.exception.DomainException;
import _bg.footballbettingapp.team.model.Team;
import _bg.footballbettingapp.team.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeamService {

    private final TeamRepository teamRepository;

    @Autowired
    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }


    public Team getByName(String name) {
        String normalizedName = name.trim();

        return teamRepository.findByNameIgnoreCase(normalizedName)
                .orElseThrow(() -> new DomainException("Team not found: '" + normalizedName + "'"));
    }
}