package com.events_cav.events_venues.domain.ports.output;

import com.events_cav.events_venues.domain.model.UserModel;

import java.util.Optional;

public interface UserRepositoryPort {
    UserModel save(UserModel userModel);
    Optional<UserModel> findByUsername(String username);
}