package _bg.footballbettingapp.match.service;

import _bg.footballbettingapp.bet.service.BetService;
import _bg.footballbettingapp.exception.DomainException;
import _bg.footballbettingapp.match.model.Match;
import _bg.footballbettingapp.match.model.MatchStatus;
import _bg.footballbettingapp.match.repository.MatchRepository;
import _bg.footballbettingapp.web.dto.FinishMatchRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class MatchAdminService {


    private final MatchRepository matchRepository;
    private final BetService betService;

    @Autowired
    public MatchAdminService(MatchRepository matchRepository, BetService betService) {
        this.matchRepository = matchRepository;
        this.betService = betService;
    }


    public List<Match> getAdminOpenMatches() {
        return matchRepository.findAllByMatchStatusInOrderByStartTimeAsc(List.of(MatchStatus.SCHEDULED, MatchStatus.IN_PROGRESS));
    }

    @Transactional
    public void finishMatch(UUID id, Integer homeGoals, Integer awayGoals) {
        Match match = matchRepository.findById(id)
                .orElseThrow(() -> new DomainException("Match not found with id: " + id));
       if (match.getMatchStatus() == MatchStatus.FINISHED){
           throw new DomainException("Match is already finished");
       }

        if (match.getMatchStatus() == MatchStatus.CANCELLED){
            throw new DomainException("Match is already cancelled");
        }


        match.setHomeGoals(homeGoals);
        match.setAwayGoals(awayGoals);
        match.setMatchStatus(MatchStatus.FINISHED);

        matchRepository.saveAndFlush(match);

        betService.settleBetForMatch(id);


    }

}
