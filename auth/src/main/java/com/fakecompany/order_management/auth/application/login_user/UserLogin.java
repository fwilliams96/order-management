package com.fakecompany.order_management.auth.application.login_user;

import com.fakecompany.order_management.auth.application.generate_jwt.JwtGenerator;
import com.fakecompany.order_management.auth.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserLogin {

    private final UserRepository userRepository;
    private final JwtGenerator jwtGenerator;
    private final PasswordEncoder passwordEncoder;

    public JWToken login(UserCredentials userCredentials) {
        User user = userRepository.findByEmail(userCredentials.getEmail())
                .orElseThrow(UserInvalidCredentialsError::new);
        boolean matches = passwordEncoder.matches(userCredentials.getPassword(), user.getPassword());
        if (!matches) {
            throw new UserInvalidCredentialsError();
        }
        return jwtGenerator.generate(user);
    }

}
