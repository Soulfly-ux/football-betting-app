package _bg.footballbettingapp.team.service;

import _bg.footballbettingapp.exception.DomainException;
import _bg.footballbettingapp.team.model.Team;
import _bg.footballbettingapp.team.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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


    public Team getById(UUID id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new DomainException("Team not found: '" + id + "'"));
    }
    @Cacheable("teamsSortedByName")
    public List<Team> getAllTeamSortedByName() {
        return teamRepository.findAllByOrderByNameAsc();
    }
}