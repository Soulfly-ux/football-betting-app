package _bg.footballbettingapp.web;

import _bg.footballbettingapp.common.model.Country;
import _bg.footballbettingapp.user.service.UserService;
import _bg.footballbettingapp.web.dto.EditProfileRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/edit")
    public ModelAndView getEditProfilePage() {
        EditProfileRequest editProfileRequest = userService.getCurrentUserProfile();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("editProfileRequest", editProfileRequest);
        modelAndView.addObject("countries", Country.values());
        modelAndView.setViewName("profile-edit");
        return modelAndView;


    }

    @PutMapping("/edit")
    public ModelAndView editProfile(@Valid EditProfileRequest editProfileRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("editProfileRequest", editProfileRequest);
            modelAndView.addObject("countries", Country.values());
            modelAndView.setViewName("profile-edit");
            return modelAndView;



        }

        userService.editProfile(editProfileRequest);
        return new ModelAndView("redirect:/home");
    }
}
