package _bg.footballbettingapp.web;

import _bg.footballbettingapp.email.service.NotificationService;
import _bg.footballbettingapp.security.AuthenticationDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributes {

    private final NotificationService notificationService;

    public GlobalModelAttributes(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @ModelAttribute("navbarUnreadNotificationCount")
    public long navbarUnreadNotificationCount(@AuthenticationPrincipal AuthenticationDetails authenticationDetails) {
        if (authenticationDetails == null) {
            return 0;
        }

        return notificationService.getUnreadNotificationCount(authenticationDetails.getUserId());
    }
}
