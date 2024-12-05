package pl.tazz.ideas.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.tazz.ideas.user.domain.model.Role;
import pl.tazz.ideas.user.domain.model.User;
import pl.tazz.ideas.user.domain.repository.RoleRepository;
import pl.tazz.ideas.user.domain.repository.UserRepository;

import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationController(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String registrationForm() {
        return "user/register"; // Wskaż widok rejestracji
    }

    @PostMapping
    public String registerUser(@RequestParam String username,
                               @RequestParam String email,
                               @RequestParam String password,
                               Model model) {
        // Walidacja: sprawdź, czy użytkownik już istnieje
        if (userRepository.existsByUsername(username)) {
            model.addAttribute("error", "Nazwa użytkownika już istnieje!");
            return "user/register";
        }

        if (userRepository.existsByEmail(email)) {
            model.addAttribute("error", "Email jest już zajęty!");
            return "user/register";
        }

        // Znajdź rolę USER
        Optional<Role> userRoleOpt = roleRepository.findByName("USER");
        if (userRoleOpt.isEmpty()) {
            model.addAttribute("error", "Rola USER nie została znaleziona!");
            return "user/register";
        }

        // Stwórz nowego użytkownika
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(Set.of(userRoleOpt.get()));

        userRepository.save(user);

        model.addAttribute("success", "Rejestracja zakończona sukcesem. Możesz się zalogować.");
        return "redirect:/login"; // Przekierowanie na stronę logowania
    }

}
