package _bg.footballbettingapp.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Set;

@Component
public class SessionCheckInterceptor implements HandlerInterceptor {


    private final Set<String> UNAUTHENTICATED_ENDPOINTS = Set.of("/login", "/register", "/");


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


        return true;
    }
}
