package _bg.footballbettingapp.team;

import _bg.footballbettingapp.exception.DomainException;
import _bg.footballbettingapp.team.model.Team;
import _bg.footballbettingapp.team.repository.TeamRepository;
import _bg.footballbettingapp.team.service.TeamService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class TeamServiceUTests {
    @Mock
    private  TeamRepository teamRepository;

    @InjectMocks
    private TeamService teamService;


    @Test
    void givenExistingTeamName_whenGetByName_thenReturnTeam() {
        String teamName = "Inter";

        Team inter = Team.builder()
                .name(teamName)
                .build();

        when(teamRepository.findByNameIgnoreCase(teamName)).thenReturn(Optional.of(inter));

        Team result = teamService.getByName(teamName);

        assertEquals(inter, result);
        verify(teamRepository).findByNameIgnoreCase(teamName);


    }


    @Test
    void givenMissingTeamName_whenGetByName_thenExceptionIsThrown() {
        String teamName = "Inter";


        when(teamRepository.findByNameIgnoreCase(teamName)).thenReturn(Optional.empty());

        assertThrows(DomainException.class, () -> teamService.getByName(teamName));
    }



    @Test
    void givenTeamNameWithExtraSpaces_whenGetByName_thenTrimAndReturnTeam() {
        String teamName = " Inter  ";

        String normalizedName = "Inter";

        Team team = Team.builder()
                .name(normalizedName)
                .build();

        when(teamRepository.findByNameIgnoreCase(normalizedName)).thenReturn(Optional.of(team));

        Team result = teamService.getByName(teamName);

        assertEquals(team, result);
        verify(teamRepository).findByNameIgnoreCase(normalizedName);
    }


    @Test
    void givenExistingTeamId_whenGetById_thenReturnTeam() {

        UUID id = UUID.randomUUID();
        Team team = Team.builder()
                .id(id)
                .build();
        when(teamRepository.findById(id)).thenReturn(Optional.of(team));

        Team result = teamService.getById(id);

        assertEquals(team, result);
        verify(teamRepository).findById(id);

    }

    @Test
    void givenMissingTeamId_whenGetById_thenExceptionIsThrown() {

        UUID id = UUID.randomUUID();
        when(teamRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(DomainException.class, () -> teamService.getById(id));

    }

    @Test
    void whenGetAllTeamSortedByName_thenReturnTeams() {

        List<Team> teams = List.of(new Team(), new Team());
        when(teamRepository.findAllByOrderByNameAsc()).thenReturn(teams);

        List<Team> result = teamService.getAllTeamSortedByName();

        assertEquals(teams, result);
        verify(teamRepository).findAllByOrderByNameAsc();

    }



}
