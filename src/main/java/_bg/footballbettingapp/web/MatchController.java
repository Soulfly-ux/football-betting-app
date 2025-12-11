package _bg.footballbettingapp.web;

import _bg.footballbettingapp.match.model.Match;
import _bg.footballbettingapp.match.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

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
}
