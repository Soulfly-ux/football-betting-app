package _bg.footballbettingapp.web;

import _bg.footballbettingapp.security.AuthenticationDetails;
import _bg.footballbettingapp.user.model.User;
import _bg.footballbettingapp.user.service.UserService;
import _bg.footballbettingapp.web.dto.LoginRequest;
import _bg.footballbettingapp.web.dto.RegisterRequest;
import ch.qos.logback.core.model.Model;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("/")
public class IndexController {

    private final UserService userService;

    @Autowired
    public IndexController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/")
    public String index() {
        return "index";

    }



    @GetMapping("/login")
    public ModelAndView login(@RequestParam(value = "error", required = false) String error) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        modelAndView.addObject("loginRequest", new LoginRequest());


        if (error != null) {
            modelAndView.addObject("error", "Invalid username or password");
        }

        return modelAndView;
    }





    @GetMapping("/register")
    public ModelAndView getRegisterPage() {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("registerRequest", new RegisterRequest());
        modelAndView.setViewName("register");

        return modelAndView;
    }

    @PostMapping ("/register")
    public ModelAndView register(@Valid RegisterRequest registerRequest, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            return new ModelAndView("register");
        }

        User registeredUser = userService.register(registerRequest);


//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.addObject("user", registeredUser);
//        modelAndView.setViewName("home");
        return new ModelAndView("redirect:/login");
    }


    @GetMapping("/home")
    public ModelAndView home(@AuthenticationPrincipal AuthenticationDetails authenticationDetails) {
        ModelAndView modelAndView = new ModelAndView();


        User currentUser = userService.getUserById(authenticationDetails.getUserId());

        modelAndView.addObject("user", currentUser);
        modelAndView.setViewName("home");

        return modelAndView;
    }



}
