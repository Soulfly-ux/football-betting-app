package _bg.footballbettingapp.exception;

import _bg.footballbettingapp.match.model.Match;
import _bg.footballbettingapp.match.service.MatchAdminService;
import _bg.footballbettingapp.match.service.MatchService;
import _bg.footballbettingapp.team.model.Team;
import _bg.footballbettingapp.team.service.TeamService;
import _bg.footballbettingapp.user.model.User;
import _bg.footballbettingapp.user.service.UserService;
import _bg.footballbettingapp.web.dto.BetRequest;
import _bg.footballbettingapp.web.dto.CreateMatchRequest;
import _bg.footballbettingapp.web.dto.EditMatchRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final MatchService matchService;
    private final MatchAdminService matchAdminService;
    private final TeamService teamService;
    private final UserService userService;

    @Autowired
    public GlobalExceptionHandler(MatchService matchService, MatchAdminService matchAdminService, TeamService teamService, UserService userService) {
        this.matchService = matchService;
        this.matchAdminService = matchAdminService;
        this.teamService = teamService;
        this.userService = userService;
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ModelAndView handleInsufficientBalanceException(InsufficientBalanceException ex) {

        UUID matchId = ex.getMatchId();
        Match match = matchService.getMatchById(matchId);


        BetRequest betRequest = new BetRequest();
        betRequest.setMatchId(matchId);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("bet-form");
        modelAndView.addObject("betRequest", betRequest);
        modelAndView.addObject("match", match);
        modelAndView.addObject("errorMessage", ex.getMessage());


        return modelAndView;
    }

    @ExceptionHandler(MatchEditException.class)
    public ModelAndView handleEditMatchException(MatchEditException exception) {
        UUID matchId = exception.getMatchId();
        Match match = matchService.getMatchById(matchId);

        EditMatchRequest editMatchRequest = matchAdminService.mapToEditMatchRequest(match);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin-match-edit");
        modelAndView.addObject("match", match);
        modelAndView.addObject("editMatchRequest", editMatchRequest);
        modelAndView.addObject("errorMessage", exception.getMessage());


        return modelAndView;

    }


    @ExceptionHandler(MatchCreateException.class)
    public ModelAndView handleMatchCreateException(MatchCreateException exception) {

        List<Team> allTeams = teamService.getAllTeamSortedByName();
        List<String> supportedLeagues = matchAdminService.getSupportedLeagues();


        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin-match-create");
        modelAndView.addObject("teams", allTeams);
        modelAndView.addObject("leagues", supportedLeagues);
        modelAndView.addObject("createMatchRequest", new CreateMatchRequest());
        modelAndView.addObject("errorMessage", exception.getMessage());

        return modelAndView;
    }


    @ExceptionHandler(MatchCancelException.class)
    public ModelAndView handleMatchCancelException(MatchCancelException exception, HttpSession session) {

        UUID userId = (UUID) session.getAttribute("user");
        User userById = userService.getUserById(userId);

        List<Match> adminOpenMatches = matchAdminService.getAdminOpenMatches();


        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin-matches");
        modelAndView.addObject("matches", adminOpenMatches);
        modelAndView.addObject("user", userById);
        modelAndView.addObject("errorMessage", exception.getMessage());


        return modelAndView;
    }


    @ExceptionHandler(DomainException.class)
    public ModelAndView handleDomainException(DomainException exception) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error");
        modelAndView.addObject("errorMessage", exception.getMessage());

        return modelAndView;

    }

}
