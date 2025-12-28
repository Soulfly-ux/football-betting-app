package _bg.footballbettingapp.security;

import _bg.footballbettingapp.user.model.Role;
import _bg.footballbettingapp.user.model.User;
import _bg.footballbettingapp.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Set;
import java.util.UUID;

@Component
public class SessionCheckInterceptor implements HandlerInterceptor {


    private final Set<String> UNAUTHENTICATED_ENDPOINTS = Set.of("/login", "/register", "/");
    private final Set<String> ADMIN_ENDPOINTS = Set.of("/users", "/reports");



    private final UserService userService;
    @Autowired
    public SessionCheckInterceptor(UserService userService) {
        this.userService = userService;
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {


        String endpoint = request.getServletPath();
        if (UNAUTHENTICATED_ENDPOINTS.contains(endpoint)) {
            return true;
        }

        HttpSession currentSession = request.getSession(false);
        if (currentSession == null) {
            response.sendRedirect("/login");
            return false;
        }

        Object user = currentSession.getAttribute("user");
        if (user == null) {
            response.sendRedirect("/login");
            return false;
        }

        UUID userId = (UUID) currentSession.getAttribute("user");
        User userById = userService.getUserById(userId);

        if (!userById.isActive()){
            currentSession.invalidate();
            response.sendRedirect("/");
            return false;
        }

         boolean isAdminEndpoint = endpoint.contains("/admin");
        if (isAdminEndpoint && userById.getRole() != Role.ADMIN){

            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.getWriter().write("You are not authorized to access this resource.");

            return false;
        }

        return true;
    }
}
