package _bg.footballbettingapp.web;


import _bg.footballbettingapp.match.model.Match;
import _bg.footballbettingapp.match.service.MatchAdminService;
import _bg.footballbettingapp.security.AuthenticationDetails;
import _bg.footballbettingapp.user.model.User;
import _bg.footballbettingapp.user.service.AdminDashboardService;
import _bg.footballbettingapp.user.service.UserService;
import _bg.footballbettingapp.web.dto.AdminDashboardStats;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

    private final AdminDashboardService adminDashboardService;
    private final UserService userService;
    private final MatchAdminService matchAdminService;

    @Autowired
    public AdminDashboardController(AdminDashboardService adminDashboardService, UserService userService, MatchAdminService matchAdminService) {
        this.adminDashboardService = adminDashboardService;
        this.userService = userService;
        this.matchAdminService = matchAdminService;
    }


    @GetMapping
    public ModelAndView showAdminDashboard(@AuthenticationPrincipal AuthenticationDetails authenticationDetails) {


        User userById = userService.getUserById(authenticationDetails.getUserId());

        AdminDashboardStats stats = adminDashboardService.getStats();





        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin-dashboard");


        modelAndView.addObject("stats", stats);
        modelAndView.addObject("user", userById);


        return modelAndView;
    }
}
