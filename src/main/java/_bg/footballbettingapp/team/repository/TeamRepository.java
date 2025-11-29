package _bg.footballbettingapp.team.repository;

import _bg.footballbettingapp.team.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TeamRepository extends JpaRepository<Team, UUID> {

       List<Team> findAll();

       Optional<Team> findByName(String name);

       Optional<Team> findById(UUID id);


}
