package com.events_cav.events_venues.infrastructure.adapters.output.jpa.repository;

import com.events_cav.events_venues.infrastructure.adapters.output.jpa.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DataUserRepository extends JpaRepository<UserEntity, Long> {
    // Metodo para buscar por username, requerido por Spring Security
    Optional<UserEntity> findByUsername(String username);
}