package _bg.footballbettingapp.web;

import _bg.footballbettingapp.common.model.Country;
import _bg.footballbettingapp.user.service.UserService;
import _bg.footballbettingapp.web.dto.EditProfileRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @PostMapping("/edit")
    public ModelAndView editProfile(@ModelAttribute EditProfileRequest editProfileRequest) {
        userService.editProfile(editProfileRequest);
        return new ModelAndView("redirect:/home");
    }
}
