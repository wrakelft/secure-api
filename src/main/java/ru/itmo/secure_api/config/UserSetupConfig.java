package ru.itmo.secure_api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.itmo.secure_api.user.AppUser;
import ru.itmo.secure_api.user.UserRepository;

import java.nio.charset.StandardCharsets;

@Configuration
public class UserSetupConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    @ConditionalOnProperty(name = "app.bootstrap.enabled", havingValue = "true")
    public CommandLineRunner createInitialUser(UserRepository userRepository, PasswordEncoder passwordEncoder, @Value("${BOOTSTRAP_USERNAME:student}") String username, @Value("${BOOTSTRAP_PASSWORD}") String password) {
        return args -> {
            if (userRepository.findByUsername(username).isPresent()) {
                return;
        }
        if (password.length() < 12 || password.getBytes(StandardCharsets.UTF_8).length > 72) {
            throw new IllegalArgumentException(
                    "Initial password must contain at least 12 characters " + "and at most 72 UTF-8 bytes"
            );
        }

        String hash = passwordEncoder.encode(password);
        AppUser user = new AppUser(username, hash);
        userRepository.save(user);
        };
    }
}
