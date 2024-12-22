package pl.tazz.ideas.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("user/login")
    public String userLoginPage() {
        return "login";
    }

    @GetMapping("/login")
    public String login(HttpServletRequest request, Model model) {
        Object errorMessage = request.getSession().getAttribute("errorMessage");
        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
            request.getSession().removeAttribute("errorMessage");
            System.out.println("LoginController: ErrorMessage - " + model.getAttribute("errorMessage"));

        }
        return "login";
    }
}
