package _bg.footballbettingapp.web;

import _bg.footballbettingapp.match.model.Match;
import _bg.footballbettingapp.match.service.MatchAdminService;
import _bg.footballbettingapp.match.service.MatchService;
import _bg.footballbettingapp.web.dto.FinishMatchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin/matches")
public class AdminMatchesController {

     private final MatchService matchService;
     private final MatchAdminService matchAdminService;

     @Autowired
    public AdminMatchesController(MatchService matchService, MatchAdminService matchAdminService) {
        this.matchService = matchService;
         this.matchAdminService = matchAdminService;
     }

    @GetMapping
    public ModelAndView getMatches() {

        List<Match> adminOpenMatches = matchAdminService.getAdminOpenMatches();


        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin-matches");
        modelAndView.addObject("matches", adminOpenMatches );



        return modelAndView;
    }


    @GetMapping("/{id}/finish")
    public ModelAndView finishMatch(@PathVariable UUID id) {

        Match match = matchService.getMatchById(id);
        ModelAndView modelAndView = new ModelAndView();
         modelAndView.addObject("finishRequest", new FinishMatchRequest());
         modelAndView.addObject("match", match);
         modelAndView.setViewName("admin-finish");

        return modelAndView;
    }
}
