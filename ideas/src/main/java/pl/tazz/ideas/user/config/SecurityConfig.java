package pl.tazz.ideas.user.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

@Configuration
public class SecurityConfig {

    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    public SecurityConfig(CustomAuthenticationFailureHandler customAuthenticationFailureHandler) {
        this.customAuthenticationFailureHandler = customAuthenticationFailureHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .securityContext(securityContext -> securityContext
                        .securityContextRepository(new HttpSessionSecurityContextRepository())
                )
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/questions/add").authenticated()
                        .anyRequest().permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                        .failureHandler(customAuthenticationFailureHandler) // handler dla błędów
                        .successHandler((HttpServletRequest request, HttpServletResponse response, Authentication authentication) -> {
                            // Ustawiam SecurityContext dla zalogowanego użytkownika - tym rozwiązujęproblem z podwójnym logowaniem
                            SecurityContextHolder.getContext().setAuthentication(authentication);

                            // Pobierzam poprzednią stronę, na której byłem przed logowaniem
                            SavedRequest savedRequest = (SavedRequest) request.getSession().getAttribute("SPRING_SECURITY_SAVED_REQUEST");
                            String targetUrl = (savedRequest != null) ? savedRequest.getRedirectUrl() : "/questions";

                            // Przekierowuje na poprzednią stronę lub stronę domyślną
                            response.sendRedirect(targetUrl);
                        })
                )
                .exceptionHandling(exceptions -> exceptions
                        .accessDeniedPage("/login")  // Przekierowanie na stronę logowania przy błędzie 403
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/questions/")
                        .permitAll()
                )
                .anonymous(anonymous -> anonymous.disable()); // Wyłączenie anonimowego użytkownika - krwi mi napsuł z formularzem :)
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}