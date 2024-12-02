package pl.tazz.ideas.user.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.SavedRequest;

import java.io.IOException;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()).authorizeHttpRequests(authorize -> authorize.requestMatchers("/admin/**").hasRole("ADMIN").requestMatchers("/user/**").hasRole("USER").anyRequest().permitAll()).formLogin(form -> form.loginPage("/login").permitAll().successHandler(new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                // Po zalogowaniu przekieruj na stronę, na której użytkownik wcześniej był
                String targetUrl = request.getSession().getAttribute("SPRING_SECURITY_SAVED_REQUEST") != null ? ((SavedRequest) request.getSession().getAttribute("SPRING_SECURITY_SAVED_REQUEST")).getRedirectUrl() : "/";
                response.sendRedirect(targetUrl);  // Przekierowanie na poprzednią stronę
            }
        })).exceptionHandling(exceptions -> exceptions.accessDeniedPage("/login")  // Przekierowanie na stronę logowania przy 403
        ).logout(logout -> logout.logoutSuccessUrl("/questions/").permitAll());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
