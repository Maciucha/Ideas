package pl.tazz.ideas.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.tazz.ideas.user.domain.model.User;
import pl.tazz.ideas.user.service.UserRegisterService;

@Controller
public class RegistrationController {


    private final UserRegisterService userRegisterService;

    public RegistrationController(UserRegisterService userRegisterService) {
        this.userRegisterService = userRegisterService;
    }


    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        try {
            userRegisterService.registerUser(user);
            redirectAttributes.addFlashAttribute("successMessage", "Rejestracja zakończona sukcesem! Sprawdź swój e-mail w celu weryfikacji.");
            return "redirect:/login?success";
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/login";
        }
    }
}
