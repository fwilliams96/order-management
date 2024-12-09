package com.fakecompany.order_management.auth.application.find;

import com.fakecompany.order_management.auth.domain.User;
import com.fakecompany.order_management.auth.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserFinder {

    private final UserRepository userRepository;

    public Optional<User> find(UUID userId) {
        return userRepository.findById(userId);
    }

}
