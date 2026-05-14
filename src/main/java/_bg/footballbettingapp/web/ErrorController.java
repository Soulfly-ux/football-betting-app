package _bg.footballbettingapp.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ErrorController {

    @GetMapping("/access-denied")
    public ModelAndView accessDenied() {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("errorMessage", "You do not have permission to access this page.");
        return modelAndView;
    }

    @GetMapping("/not-found")
    public ModelAndView notFound() {
        return new ModelAndView("not-found");
    }


}
