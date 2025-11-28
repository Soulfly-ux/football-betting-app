package _bg.footballbettingapp.bet.repository;

import _bg.footballbettingapp.bet.model.Bet;
import _bg.footballbettingapp.bet.model.BetStatus;
import _bg.footballbettingapp.match.model.Match;
import _bg.footballbettingapp.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BetRepository extends JpaRepository<Bet, UUID> {

    List<Bet> findAllByUserOrderByCreatedOnDesc(User user);


    List<Bet> findAllByMatchAndBetStatus(Match match, BetStatus betStatus);


}
