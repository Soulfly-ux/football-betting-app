package _bg.footballbettingapp.web;

import _bg.footballbettingapp.bet.model.Bet;
import _bg.footballbettingapp.bet.service.BetService;
import _bg.footballbettingapp.match.model.Match;
import _bg.footballbettingapp.match.service.MatchService;
import _bg.footballbettingapp.user.model.User;
import _bg.footballbettingapp.user.service.UserService;
import _bg.footballbettingapp.web.dto.BetRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/bets")
public class BetController {

    private final BetService betService;
    private final MatchService matchService;
    private final UserService userService;

    @Autowired


    public BetController(BetService betService, MatchService matchService, UserService userService) {
        this.betService = betService;
        this.matchService = matchService;
        this.userService = userService;
    }

    @PostMapping
    public ModelAndView placeBet(@Valid BetRequest betRequest, BindingResult bindingResult, HttpSession session) {


        UUID userId = (UUID) session.getAttribute("user");


        if (bindingResult.hasErrors()) {
            Match matchById = matchService.getMatchById(betRequest.getMatchId());


            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("bet-form");
            modelAndView.addObject("match", matchById);
            modelAndView.addObject("betRequest", betRequest);
            modelAndView.addObject("errorMessage", null);
            return modelAndView;
        }

        User currentUser = userService.getUserById(userId);


        betService.placeBet(
                currentUser.getId(),
                betRequest.getMatchId(),
                betRequest.getBetType(),
                betRequest.getStake()
        );


        return new ModelAndView("redirect:/my-bets");

    }


    @GetMapping("/my-bets")
    public ModelAndView myBets(HttpSession session) {


        UUID userId = (UUID) session.getAttribute("user");
        User user = userService.getUserById(userId);


        List<Bet> betsByUser = betService.getBetsByUser(user);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("my-bets");
        modelAndView.addObject("user", user);
        modelAndView.addObject("bets", betsByUser);


        return modelAndView;

    }

}
