package _bg.footballbettingapp.web;

import _bg.footballbettingapp.common.model.Country;
import _bg.footballbettingapp.security.AuthenticationDetails;
import _bg.footballbettingapp.user.model.User;
import _bg.footballbettingapp.user.service.UserService;
import _bg.footballbettingapp.web.dto.EditProfileRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }







    @GetMapping("/edit")
    public ModelAndView getEditProfilePage(@AuthenticationPrincipal AuthenticationDetails authenticationDetails) {

        UUID userId = authenticationDetails.getUserId();
        User currentUser = userService.getUserById(userId);   // вземам user
        EditProfileRequest editProfileRequest = userService.getCurrentUserProfile(userId);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("editProfileRequest", editProfileRequest);
        modelAndView.addObject("countries", Country.values());
        modelAndView.setViewName("profile-edit");
        return modelAndView;


    }

    @PutMapping("/edit")
    public ModelAndView editProfile(@Valid EditProfileRequest editProfileRequest, BindingResult bindingResult, @AuthenticationPrincipal AuthenticationDetails authenticationDetails) {


        UUID userId = authenticationDetails.getUserId();

        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("editProfileRequest", editProfileRequest);
            modelAndView.addObject("countries", Country.values());
            modelAndView.setViewName("profile-edit");
            return modelAndView;



        }



        userService.editProfile(editProfileRequest, userId);
        return new ModelAndView("redirect:/home");
    }
}
