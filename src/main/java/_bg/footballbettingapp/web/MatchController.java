package _bg.footballbettingapp.web;

import _bg.footballbettingapp.match.model.Match;
import _bg.footballbettingapp.match.service.MatchService;
import _bg.footballbettingapp.web.dto.BetRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/matches")
public class MatchController {

    private final MatchService matchService;
    @Autowired
    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

     @GetMapping
    public ModelAndView getMatches() {
        List<Match> upcomingMatches = matchService.getUpcomingMatches();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("matches");
        modelAndView.addObject("matches", upcomingMatches);


        return modelAndView;
    }


    @GetMapping("/{id}/bet")
    public ModelAndView getMatchById(@PathVariable UUID id) {
        Match matchById = matchService.getMatchById(id);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("bet-form");

        BetRequest betRequest = new BetRequest();
        betRequest.setMatchId(id);
        modelAndView.addObject("betRequest",betRequest);
        modelAndView.addObject("match", matchById);

        return modelAndView;
    }
}
