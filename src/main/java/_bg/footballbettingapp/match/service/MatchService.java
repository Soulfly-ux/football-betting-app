package _bg.footballbettingapp.match.service;

import _bg.footballbettingapp.exception.DomainException;
import _bg.footballbettingapp.match.model.Match;
import _bg.footballbettingapp.match.model.MatchStatus;
import _bg.footballbettingapp.match.repository.MatchRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class MatchService {


    private final MatchRepository matchRepository;
    @Autowired
    public MatchService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }


    public List<Match> getUpcomingMatches() {
        LocalDateTime now = LocalDateTime.now();

        return matchRepository.findAllByMatchStatusAndStartTimeAfterOrderByStartTimeAsc(MatchStatus.SCHEDULED,now);
    }

    public Match getMatchById(UUID id) {
        return matchRepository.findById(id)
                .orElseThrow(() -> new DomainException("Match not found"));
    }

    @Transactional
    public Match setResult(UUID matchId, int homeGoals, int awayGoals) {

        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new DomainException("Match not found"));


        if (match.getMatchStatus() == MatchStatus.CANCELLED) {
            throw new DomainException("Match is already cancelled");
        }

        if (match.getMatchStatus() == MatchStatus.FINISHED) {
            throw new DomainException("Match is already finished");
        }

        match.setHomeGoals(homeGoals);
        match.setAwayGoals(awayGoals);
        match.setMatchStatus(MatchStatus.FINISHED);

        return matchRepository.save(match);
    }

    public String getResultAsString(Match match) {
        if(match.getHomeGoals() == null || match.getAwayGoals() == null) {
            return "N/A";
        }

        return match.getHomeGoals() + ":" + match.getAwayGoals();
    }


    public long countMatches() {
        return matchRepository.count();
    }

    public void save(Match match) {
        matchRepository.save(match);
    }

}
