package pl.tazz.ideas.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.tazz.ideas.user.domain.model.Role;
import pl.tazz.ideas.user.domain.model.User;
import pl.tazz.ideas.user.domain.repository.RoleRepository;
import pl.tazz.ideas.user.domain.repository.UserRepository;

import java.util.HashSet;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserRegisterService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailSendService emailSendService;

    public boolean verifyEmail(String token) {
        User user = userRepository.findByVerifyEmailToken(token).orElse(null);
        if (user != null) {
            user.setEmailVerified(true);
            user.setVerifyEmailToken(null); // Resetowanie tokenu po weryfikacji
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public String generateVerificationToken() {
        return UUID.randomUUID().toString();
    }

    public void registerUser(User user) {

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Użytkownik o tym loginie już istnieje.");
        }

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Użytkownik o tym adresie e-mail już istnieje.");
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        String token = generateVerificationToken();
        user.setVerifyEmailToken(token);

        Role userRole = roleRepository.findByName("USER")
                .orElseGet(() -> createDefaultRole("USER"));

        if (user.getRoles() == null) {
            user.setRoles(new HashSet<>());
        }
        user.getRoles().add(userRole);

        userRepository.save(user);

        emailSendService.sendVerificationEmail(user.getEmail(), token);
    }

    // Pomocnicza metoda do tworzenia domyślnej roli
    private Role createDefaultRole(String roleName) {
        Role newRole = new Role();
        newRole.setName(roleName);
        return roleRepository.save(newRole);
    }


}
