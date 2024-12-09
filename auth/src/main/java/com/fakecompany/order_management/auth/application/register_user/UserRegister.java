package com.fakecompany.order_management.auth.application.register_user;

import com.fakecompany.order_management.auth.application.generate_jwt.JwtGenerator;
import com.fakecompany.order_management.auth.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserRegister {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtGenerator jwtGenerator;

    public JWToken register(NewUser newUser) {
        Optional<User> byEmail = userRepository.findByEmail(newUser.getEmail());
        if (byEmail.isPresent()) {
            throw new UserAlreadyExistsError(newUser.getEmail());
        }
        User user = userRepository.create(
                User.builder()
                        .id(UUID.randomUUID())
                        .name(newUser.getName())
                        .email(newUser.getEmail())
                        .password(passwordEncoder.encode(newUser.getPassword()))
                        .roles(Set.of("USER", "ADMIN"))
                        .build()
        );
        return jwtGenerator.generate(user);
    }

}
