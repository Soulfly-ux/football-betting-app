package _bg.footballbettingapp.web;

import _bg.footballbettingapp.user.model.User;
import _bg.footballbettingapp.user.service.UserAdminService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
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
public class UserAdminController {


    private final UserAdminService userAdminService;


    @Autowired
    public UserAdminController(UserAdminService userAdminService) {
        this.userAdminService = userAdminService;
    }

    @GetMapping
    public ModelAndView getAllUsers(HttpSession session) {

        UUID sessionUserId = (UUID) session.getAttribute("user");

        List<User> users = userAdminService.getAllUsers();

        long count = users.stream().filter(User::isActive).count();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin-users");
        modelAndView.addObject("users", users);
        modelAndView.addObject("activeCount", count);
        modelAndView.addObject("sessionUserId ", sessionUserId);

        return modelAndView;
    }



    @PutMapping("/{id}/role")
    public String switchUserRole(@PathVariable UUID id) {




        userAdminService.switchRole(id);

        return "redirect:/admin/users";
    }


    @PutMapping("/{id}/status")
    public String switchUserStatus(@PathVariable UUID id) {

        userAdminService.switchStatus(id);

        return "redirect:/admin/users";
    }
}
