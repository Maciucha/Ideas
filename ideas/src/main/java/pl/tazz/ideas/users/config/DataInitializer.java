package pl.tazz.ideas.users.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.tazz.ideas.users.domain.model.User;
import pl.tazz.ideas.users.domain.model.Role;
import pl.tazz.ideas.users.domain.repository.RoleRepository;
import pl.tazz.ideas.users.domain.repository.UserRepository;

import java.beans.Transient;
import java.util.Optional;
import java.util.Set;

@Getter
@Setter
@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        // Sprawdzanie czy role już istnieją w bazie, aby uniknąć zapisu tego samego obiektu wielokrotnie
        if (roleRepository.count() == 0) {
            Role adminRole = new Role();
            adminRole.setName("ADMIN");
            roleRepository.save(adminRole);

            Role userRole = new Role();
            userRole.setName("USER");
            roleRepository.save(userRole);
        }

        // Pobieranie ról z bazy danych
        Optional<Role> adminRoleOpt = roleRepository.findByName("ADMIN");
        Optional<Role> userRoleOpt = roleRepository.findByName("USER");

        if (adminRoleOpt.isPresent() && userRoleOpt.isPresent()) {
            Role adminRole = roleRepository.findById(adminRoleOpt.get().getId()).orElseThrow();
            Role userRole = roleRepository.findById(userRoleOpt.get().getId()).orElseThrow();

            if (userRepository.count() == 0) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRoles(Set.of(adminRole));
                userRepository.save(admin);

                User user = new User();
                user.setUsername("user");
                user.setPassword(passwordEncoder.encode("user123"));
                user.setRoles(Set.of(userRole));
                userRepository.save(user);
            }
        }
    }
}