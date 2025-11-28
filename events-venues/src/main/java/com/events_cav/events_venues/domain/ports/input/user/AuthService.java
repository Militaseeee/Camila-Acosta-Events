package com.events_cav.events_venues.domain.ports.input.user;

import com.events_cav.events_venues.domain.model.user.UserLoginCommand;
import com.events_cav.events_venues.domain.model.user.UserRegisterCommand;

public interface AuthService {

    void register(UserRegisterCommand command);

    String authenticate(UserLoginCommand command);
}