package _bg.footballbettingapp.exception;

import _bg.footballbettingapp.match.model.Match;
import _bg.footballbettingapp.match.service.MatchAdminService;
import _bg.footballbettingapp.match.service.MatchService;
import _bg.footballbettingapp.security.AuthenticationDetails;
import _bg.footballbettingapp.team.model.Team;
import _bg.footballbettingapp.team.service.TeamService;
import _bg.footballbettingapp.user.model.User;
import _bg.footballbettingapp.user.service.UserService;
import _bg.footballbettingapp.web.dto.BetRequest;
import _bg.footballbettingapp.web.dto.CreateMatchRequest;
import _bg.footballbettingapp.web.dto.EditMatchRequest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.NoResourceFoundException;


import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;
@Slf4j
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
    public ModelAndView handleMatchCancelException(MatchCancelException exception, AuthenticationDetails authenticationDetails) {
        UUID userId = authenticationDetails.getUserId();
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

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({
            NoResourceFoundException.class,
                    MethodArgumentTypeMismatchException.class

            })
    public ModelAndView handleNotFoundExceptions(Exception exception) {

        log.warn("Not found error occurred: {}", exception.getMessage());

        return new ModelAndView("not-found");
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView handleAccessDeniedException(Exception exception) {

        log.warn("Access denied: {}", exception.getMessage());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error");
        modelAndView.addObject("errorMessage", "You do not have permission to access this resource." );

          return modelAndView;
    }



    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ModelAndView handleAnyException(Exception e) {


        log.error("Unexpected error occurred in football-betting-app.", e);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error");
        modelAndView.addObject("errorMessage", "An unexpected error occurred. Please try again.");

       // e.printStackTrace();показва реалната грешка. По- професионално обаче е да ползвам log.error, a това е за учебna цел.

        return modelAndView;
    }

}
