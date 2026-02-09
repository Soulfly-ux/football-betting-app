package _bg.footballbettingapp.web;

import _bg.footballbettingapp.match.model.Match;
import _bg.footballbettingapp.match.service.MatchAdminService;
import _bg.footballbettingapp.match.service.MatchService;
import _bg.footballbettingapp.security.AuthenticationDetails;
import _bg.footballbettingapp.team.model.Team;
import _bg.footballbettingapp.team.service.TeamService;
import _bg.footballbettingapp.user.model.User;
import _bg.footballbettingapp.user.service.UserService;
import _bg.footballbettingapp.web.dto.CreateMatchRequest;
import _bg.footballbettingapp.web.dto.EditMatchRequest;
import _bg.footballbettingapp.web.dto.FinishMatchRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin/matches")
public class AdminMatchesController {

     private final MatchService matchService;
     private final MatchAdminService matchAdminService;
     private final TeamService teamService;
     private final UserService userService;

     @Autowired
    public AdminMatchesController(MatchService matchService, MatchAdminService matchAdminService, TeamService teamService, UserService userService) {
        this.matchService = matchService;
         this.matchAdminService = matchAdminService;
         this.teamService = teamService;
         this.userService = userService;
     }

    @GetMapping
    public ModelAndView getMatches(@AuthenticationPrincipal AuthenticationDetails authenticationDetails) {

        int staleHours = 8;
        List<Match> staleInProgressMatches = matchAdminService.getStaleInProgressMatches(staleHours);
        User user = userService.getUserById(authenticationDetails.getUserId());



        List<Match> adminOpenMatches = matchAdminService.getAdminOpenMatches();

        List<Match> overdueMatches = matchAdminService.getOverdueMatches();
        List<Match> scheduledMatches = matchAdminService.getScheduledMatches();
        List<Match> inProgressMatches = matchAdminService.getInProgressMatches();
        List<Match> finishedMatches = matchAdminService.getFinishedMatches();
        List<Match> cancelledMatches = matchAdminService.getCancelledMatches();


        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin-matches");
        modelAndView.addObject("matches", adminOpenMatches );
        modelAndView.addObject("user", user);
        modelAndView.addObject("now", LocalDateTime.now());
        modelAndView.addObject("overdueMatches", overdueMatches);
        modelAndView.addObject("scheduledMatches", scheduledMatches);
        modelAndView.addObject("inProgressMatches", inProgressMatches);
        modelAndView.addObject("finishedMatches", finishedMatches);
        modelAndView.addObject("cancelledMatches", cancelledMatches);
        modelAndView.addObject("staleHours", staleHours);
        modelAndView.addObject("staleInProgressMatches", staleInProgressMatches);



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

    @PutMapping("/{id}/finish")
    public ModelAndView finishMatch(@PathVariable UUID id, @Valid FinishMatchRequest finishRequest, BindingResult bindingResult ) {




        if (bindingResult.hasErrors()){

            Match matchById = matchService.getMatchById(id);
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("match", matchById);
            modelAndView.addObject("finishRequest", finishRequest); // добавям ги защото ми трябват за "admin-finish" view
            modelAndView.setViewName("admin-finish");

              return modelAndView;
         }



//
//         modelAndView.setViewName("redirect:/admin/matches"); -> тези не ми трябват, защото редиректвам към гет заявката
//         modelAndView.addObject("match", matchById);
//         modelAndView.addObject("finishRequest", finishRequest);

        matchAdminService.finishMatch(id, finishRequest.getHomeGoals(), finishRequest.getAwayGoals());
        return new ModelAndView("redirect:/admin/matches");
    }



    @GetMapping("/new")
    public ModelAndView createNewMatch() {

        List<Team> allTeams = teamService.getAllTeamSortedByName();

        List<String> supportedLeagues = matchAdminService.getSupportedLeagues();


        ModelAndView modelAndView = new ModelAndView();
         modelAndView.setViewName("admin-match-create");
         modelAndView.addObject("teams", allTeams);
         modelAndView.addObject("leagues", supportedLeagues);
         modelAndView.addObject("createMatchRequest", new CreateMatchRequest());



        return modelAndView;
    }

    @PostMapping("/new")
    public ModelAndView createNewMatch(@Valid CreateMatchRequest createMatchRequest, BindingResult bindingResult) {

         if (bindingResult.hasErrors()) {
             ModelAndView modelAndView = new ModelAndView();
             List<Team> allTeams = teamService.getAllTeamSortedByName();
             List<String> supportedLeagues = matchAdminService.getSupportedLeagues();
             modelAndView.setViewName("admin-match-create");
             modelAndView.addObject("leagues", supportedLeagues);
             modelAndView.addObject("teams", allTeams);
             modelAndView.addObject("createMatchRequest", createMatchRequest);


             return modelAndView;
         }



      matchAdminService.createNewMatch(createMatchRequest);

         return new ModelAndView("redirect:/admin/matches");
    }


    @GetMapping("/{id}/edit")
    public ModelAndView editMatch(@PathVariable UUID id) {

        Match match = matchService.getMatchById(id);
        EditMatchRequest editMatchRequest = matchAdminService.mapToEditMatchRequest(match);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("match", match);
        modelAndView.addObject("editMatchRequest", editMatchRequest);
        modelAndView.setViewName("admin-match-edit");


        return modelAndView;
    }


    @PutMapping("/{id}")
    public ModelAndView editMatch(@PathVariable UUID id,@Valid EditMatchRequest editMatchRequest, BindingResult bindingResult) {

         if (bindingResult.hasErrors()) {

             ModelAndView modelAndView = new ModelAndView();
             Match match = matchService.getMatchById(id);
             modelAndView.setViewName("admin-match-edit");
             modelAndView.addObject("match", match);
             modelAndView.addObject("editMatchRequest", editMatchRequest);


             return modelAndView;
         }

         matchAdminService.editMatch(editMatchRequest, id);

         return new ModelAndView("redirect:/admin/matches");
    }


    @PutMapping("/{id}/cancel")
    public String cancelMatch(@PathVariable UUID id) {


        matchAdminService.cancelMatch(id);
        return ("redirect:/admin/matches");
    }
}
