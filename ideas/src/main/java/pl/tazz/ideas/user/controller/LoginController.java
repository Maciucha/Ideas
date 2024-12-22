package pl.tazz.ideas.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("user/login")
    public String userLoginPage() {
        return "user/login";
    }

    @GetMapping("login")
    public String LoginPage() {
        return  "login";
    }
}
