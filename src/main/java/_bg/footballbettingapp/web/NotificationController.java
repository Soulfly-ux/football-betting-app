package _bg.footballbettingapp.web;

import _bg.footballbettingapp.email.client.dto.NotificationResponse;
import _bg.footballbettingapp.email.service.NotificationService;
import _bg.footballbettingapp.security.AuthenticationDetails;
import _bg.footballbettingapp.user.model.User;
import _bg.footballbettingapp.user.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final UserService userService;

    public NotificationController(NotificationService notificationService, UserService userService) {
        this.notificationService = notificationService;
        this.userService = userService;
    }

    @GetMapping
    public ModelAndView getNotifications(@AuthenticationPrincipal AuthenticationDetails authenticationDetails) {
        UUID userId = authenticationDetails.getUserId();
        User user = userService.getUserById(userId);
        List<NotificationResponse> notifications = notificationService.getNotifications(userId);
        long unreadCount = notificationService.getUnreadNotificationCount(userId);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("notifications");
        modelAndView.addObject("user", user);
        modelAndView.addObject("notifications", notifications);
        modelAndView.addObject("unreadCount", unreadCount);

        return modelAndView;
    }

    @PostMapping("/{notificationId}/read")
    public ModelAndView markAsRead(@PathVariable UUID notificationId) {
        notificationService.markAsRead(notificationId);

        return new ModelAndView("redirect:/notifications");
    }

    @PostMapping("/{notificationId}/delete")
    public ModelAndView deleteNotification(@PathVariable UUID notificationId) {
        notificationService.deleteNotification(notificationId);

        return new ModelAndView("redirect:/notifications");
    }
}
