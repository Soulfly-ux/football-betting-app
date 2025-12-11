package _bg.footballbettingapp.web;

import _bg.footballbettingapp.user.model.User;
import _bg.footballbettingapp.user.service.UserService;
import _bg.footballbettingapp.web.dto.LoginRequest;
import _bg.footballbettingapp.web.dto.RegisterRequest;
import ch.qos.logback.core.model.Model;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @GetMapping("/home")
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView();
        User currentUser = userService.getCurrentUser();
        modelAndView.addObject("user", currentUser);
        modelAndView.setViewName("home");

        return modelAndView;
    }


    @GetMapping("/login")
    public ModelAndView login() {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        modelAndView.addObject("loginRequest", new LoginRequest());

        return modelAndView;
    }

    @PostMapping("/login")
    public ModelAndView login(@Valid LoginRequest loginRequest, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            return new ModelAndView("login");
        }

        User loggedInUser = userService.login(loginRequest);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", loggedInUser);
        modelAndView.setViewName("redirect:/home");

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

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", registeredUser);
        modelAndView.setViewName("home");
        return modelAndView;
    }



}
