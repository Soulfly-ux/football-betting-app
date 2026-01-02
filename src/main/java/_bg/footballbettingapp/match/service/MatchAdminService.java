package _bg.footballbettingapp.match.service;

import _bg.footballbettingapp.match.model.Match;
import _bg.footballbettingapp.match.model.MatchStatus;
import _bg.footballbettingapp.match.repository.MatchRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class MatchAdminService {


    private final MatchRepository matchRepository;

    @Autowired
    public MatchAdminService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }


    public List<Match> getAdminOpenMatches() {
        return matchRepository.findAllByMatchStatusInOrderByStartTimeAsc(List.of(MatchStatus.SCHEDULED, MatchStatus.IN_PROGRESS));
    }
}
