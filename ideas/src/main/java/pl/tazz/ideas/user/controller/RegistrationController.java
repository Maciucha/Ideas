package pl.tazz.ideas.user.controller;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.tazz.ideas.user.domain.model.User;
import pl.tazz.ideas.user.service.UserRegisterService;

import java.util.Locale;

@Controller
public class RegistrationController {


    private final UserRegisterService userRegisterService;
    private final MessageSource messageSource;

    public RegistrationController(UserRegisterService userRegisterService, MessageSource messageSource) {
        this.userRegisterService = userRegisterService;
        this.messageSource = messageSource;
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, RedirectAttributes redirectAttributes, Locale locale) {
        try {
            userRegisterService.registerUser(user);
            String successMessage = messageSource.getMessage("successMessage.registration", null, locale);
            redirectAttributes.addFlashAttribute("successMessage", successMessage);
            return "redirect:/login?success";
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/login";
        }
    }
}
