package com.fakecompany.order_management.auth.infrastructure.persistence.mongodb;

import com.fakecompany.order_management.auth.domain.User;
import com.fakecompany.order_management.auth.domain.UserRepository;
import com.fakecompany.order_management.auth.infrastructure.persistence.mongodb.model.UserEntity;
import com.fakecompany.order_management.auth.infrastructure.persistence.mongodb.repository.SpringDataMongoDbUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MongoDbUserRepository implements UserRepository {

    private final SpringDataMongoDbUserRepository springDataMongoDbUserRepository;

    @Override
    public Optional<User> findById(UUID userId) {
        Optional<UserEntity> byId = springDataMongoDbUserRepository.findById(userId);
        return byId.map(this::mapUserEntityToUser);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        Optional<UserEntity> byEmail = springDataMongoDbUserRepository.findByEmail(email);
        return byEmail.map(this::mapUserEntityToUser);
    }

    @Override
    public User create(User user) {
        UserEntity insert = springDataMongoDbUserRepository.insert(mapUserToUserEntity(user));
        return mapUserEntityToUser(insert);
    }

    private UserEntity mapUserToUserEntity(User user) {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(user.getEmail());
        userEntity.setPassword(user.getPassword());
        userEntity.setRoles(user.getRoles());
        userEntity.setName(user.getName());
        return userEntity;
    }

    private User mapUserEntityToUser(UserEntity userEntity) {
        return User.builder()
                .id(userEntity.getId())
                .email(userEntity.getEmail())
                .password(userEntity.getPassword())
                .roles(userEntity.getRoles())
                .name(userEntity.getName())
                .build();
    }
}
