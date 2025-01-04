package pl.tazz.ideas.user.config;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SecurityException.class)
    public String handleSecurityException(SecurityException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", "Brak autoryzacji. Zaloguj się, aby uzyskać dostęp.");
        return "redirect:/login";
    }

    @ExceptionHandler(AuthenticationException.class)
    public String handleAuthenticationException(AuthenticationException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", "Nieprawidłowe dane logowania.");
        return "redirect:/login";
    }

    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDeniedException(AccessDeniedException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", "Nie masz wystarczających uprawnień, aby uzyskać dostęp.");
        return "redirect:/login";
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", "Wystąpił błąd autoryzacji: Zaloguj się.");
        return "redirect:/login";
    }
}
