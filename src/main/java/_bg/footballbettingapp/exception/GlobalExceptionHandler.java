package _bg.footballbettingapp.exception;

import _bg.footballbettingapp.match.model.Match;
import _bg.footballbettingapp.match.service.MatchService;
import _bg.footballbettingapp.web.dto.BetRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final MatchService matchService;

    @Autowired
    public GlobalExceptionHandler(MatchService matchService) {
        this.matchService = matchService;
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
}
