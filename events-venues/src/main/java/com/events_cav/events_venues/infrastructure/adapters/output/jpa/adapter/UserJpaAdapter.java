package com.events_cav.events_venues.infrastructure.adapters.output.jpa.adapter;

import com.events_cav.events_venues.domain.model.UserModel;
import com.events_cav.events_venues.domain.ports.output.UserRepositoryPort;
import com.events_cav.events_venues.infrastructure.adapters.output.jpa.entity.UserEntity;
import com.events_cav.events_venues.infrastructure.adapters.output.jpa.mapper.UserPersistenceMapper;
import com.events_cav.events_venues.infrastructure.adapters.output.jpa.repository.DataUserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

// Usamos @Component ya que implementa un Puerto (Output Port)
@Component
@RequiredArgsConstructor
public class UserJpaAdapter implements UserRepositoryPort {

    // Inyección de dependencias de Infraestructura
    private final DataUserRepository dataUserRepository;
    private final UserPersistenceMapper userPersistenceMapper;

    // Implementación del Puerto: Persistencia
    @Override
    public UserModel save(UserModel userModel) {
        // Mapear Modelo de Dominio (puro) a Entidad JPA (Infraestructura)
        UserEntity userEntity = userPersistenceMapper.toUserEntity(userModel);

        // Usar el Repository para guardar en DB
        UserEntity savedEntity = dataUserRepository.save(userEntity);

        // Mapear la Entidad guardada de vuelta a Modelo de Dominio (para devolver el ID/estado final)
        return userPersistenceMapper.toUserModel(savedEntity);
    }

    // Implementación del Puerto: Búsqueda (Ejemplo)
    @Override
    public Optional<UserModel> findByUsername(String username) {
        return dataUserRepository.findByUsername(username)
                // Mapear la Optional<Entity> a Optional<Model> usando el mapper
                .map(userPersistenceMapper::toUserModel);
    }

    @Override
    public boolean existsByUsername(String username) {
        // Delegar la llamada al repositorio de JPA
        return dataUserRepository.existsByUsername(username);
    }
}