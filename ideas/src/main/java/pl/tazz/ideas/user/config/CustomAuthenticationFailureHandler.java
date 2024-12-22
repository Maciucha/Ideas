package pl.tazz.ideas.user.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import pl.tazz.ideas.user.service.EmailNotVerifiedException;

import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final MessageSource messageSource;

    public CustomAuthenticationFailureHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String errorMessage;
        if (exception.getCause() instanceof EmailNotVerifiedException) {
            errorMessage = messageSource.getMessage("error.emailNotVerified", null, LocaleContextHolder.getLocale());
        } else {
            errorMessage = messageSource.getMessage("error.invalidUsernameOrPassword", null, LocaleContextHolder.getLocale());
        }
        request.getSession().setAttribute("errorMessage", errorMessage);
        response.sendRedirect("/login");
    }
}

