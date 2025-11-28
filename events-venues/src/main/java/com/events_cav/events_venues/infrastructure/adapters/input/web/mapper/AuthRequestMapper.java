package com.events_cav.events_venues.infrastructure.adapters.input.web.mapper;

import com.events_cav.events_venues.domain.model.user.UserLoginCommand;
import com.events_cav.events_venues.domain.model.user.UserRegisterCommand;
import com.events_cav.events_venues.infrastructure.adapters.input.web.dto.request.LoginRequest;
import com.events_cav.events_venues.infrastructure.adapters.input.web.dto.request.RegisterRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring") // Indica que es un componente de Spring y MapStruct
public interface AuthRequestMapper {

    @Mapping(target = "username", source = "username")
    @Mapping(target = "password", source = "password")
    @Mapping(target = "role", source = "role")
    UserRegisterCommand toUserRegisterCommand(RegisterRequest request);

    @Mapping(target = "username", source = "username")
    @Mapping(target = "password", source = "password")
    UserLoginCommand toUserLoginCommand(LoginRequest request);
}