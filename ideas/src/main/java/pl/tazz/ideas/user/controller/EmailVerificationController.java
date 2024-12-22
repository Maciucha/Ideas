package pl.tazz.ideas.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.tazz.ideas.user.service.UserRegisterService;

@Controller
public class EmailVerificationController {

    private final UserRegisterService userRegisterService;

    public EmailVerificationController(UserRegisterService userRegisterService) {
        this.userRegisterService = userRegisterService;
    }

    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam("token") String token, RedirectAttributes redirectAttributes) {
        boolean isVerified = userRegisterService.verifyEmail(token);

        if (isVerified) {
            redirectAttributes.addFlashAttribute("successMessage", "E-mail został potwierdzony! Możesz się teraz zalogować.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Weryfikacja e-maila nie powiodła się. Token jest nieprawidłowy lub wygasł.");
        }

        return "redirect:/login";
    }
}

