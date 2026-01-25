package _bg.footballbettingapp.web;

import _bg.footballbettingapp.security.AuthenticationDetails;
import _bg.footballbettingapp.user.model.User;
import _bg.footballbettingapp.user.service.UserAdminService;
import _bg.footballbettingapp.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {


    private final UserAdminService userAdminService;
    private final UserService userService;


    @Autowired
    public AdminUserController(UserAdminService userAdminService, UserService userService) {
        this.userAdminService = userAdminService;
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView getAllUsers(@AuthenticationPrincipal AuthenticationDetails authenticationDetails) {



        User userById = userService.getUserById(authenticationDetails.getUserId());

        List<User> users = userAdminService.getAllUsers();

        long countActiveUsers = userAdminService.countActiveUsers();

        long admins = userAdminService.countAdmins();


        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin-users");
        modelAndView.addObject("users", users);
        modelAndView.addObject("activeCount", countActiveUsers);
        modelAndView.addObject("adminsCount", admins);
        modelAndView.addObject("sessionUserId", authenticationDetails.getUserId());
        modelAndView.addObject("user",userById);

        return modelAndView;
    }



    @PutMapping("/{targetUserId}/role")
    public String switchUserRole(@PathVariable UUID targetUserId, @AuthenticationPrincipal AuthenticationDetails authenticationDetails) {

        //UUID targetUserId - id на потребителя, чиято роля ще сменя като админ(= id-то от URL: /{id}/role)
        // actorUserId - id на админа, който сменя ролята на друг потребител

        UUID actorUserId = authenticationDetails.getUserId();



       userAdminService.switchRole(targetUserId,actorUserId);

        return "redirect:/admin/users";
    }


    @PutMapping("/{id}/status")
    public String switchUserStatus(@PathVariable UUID id) {

        userAdminService.switchStatus(id);

        return "redirect:/admin/users";
    }
}
