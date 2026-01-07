package _bg.footballbettingapp.exception;

import _bg.footballbettingapp.match.model.Match;
import _bg.footballbettingapp.match.service.MatchAdminService;
import _bg.footballbettingapp.match.service.MatchService;
import _bg.footballbettingapp.web.dto.BetRequest;
import _bg.footballbettingapp.web.dto.EditMatchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final MatchService matchService;
    private final MatchAdminService matchAdminService;

    @Autowired
    public GlobalExceptionHandler(MatchService matchService, MatchAdminService matchAdminService) {
        this.matchService = matchService;
        this.matchAdminService = matchAdminService;
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


}
