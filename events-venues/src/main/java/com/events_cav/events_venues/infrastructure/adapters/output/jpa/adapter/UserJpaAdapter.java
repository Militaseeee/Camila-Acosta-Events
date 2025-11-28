package com.events_cav.events_venues.infrastructure.adapters.output.jpa.adapter;

import com.events_cav.events_venues.domain.model.UserModel;
import com.events_cav.events_venues.domain.ports.output.UserRepositoryPort;
import com.events_cav.events_venues.infrastructure.adapters.output.jpa.entity.UserEntity;
import com.events_cav.events_venues.infrastructure.adapters.output.jpa.repository.DataUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserJpaAdapter implements UserRepositoryPort {

    private final DataUserRepository dataUserRepository;
    // Si usaras MapStruct, inyectarías UserMapper aquí.

    // Conversión manual (simplificada)
    private UserModel toUserModel(UserEntity entity) {
        return UserModel.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .password(entity.getPassword())
                .role(entity.getRole())
                .build();
    }

    private UserEntity toUserEntity(UserModel model) {
        return UserEntity.builder()
                .id(model.getId())
                .username(model.getUsername())
                .password(model.getPassword())
                .role(model.getRole())
                .build();
    }

    @Override
    public UserModel save(UserModel userModel) {
        UserEntity entity = toUserEntity(userModel);
        UserEntity savedEntity = dataUserRepository.save(entity);
        return toUserModel(savedEntity);
    }

    @Override
    public Optional<UserModel> findByUsername(String username) {
        return dataUserRepository.findByUsername(username)
                .map(this::toUserModel);
    }
}