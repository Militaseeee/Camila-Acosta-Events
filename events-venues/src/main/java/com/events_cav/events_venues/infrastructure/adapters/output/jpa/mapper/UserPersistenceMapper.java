package com.events_cav.events_venues.infrastructure.adapters.output.jpa.mapper;

import com.events_cav.events_venues.domain.model.UserModel;
import com.events_cav.events_venues.infrastructure.adapters.output.jpa.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserPersistenceMapper {

    // Convierte el Modelo de Dominio puro al objeto Entity de JPA para guardar en DB
    UserEntity toUserEntity(UserModel userModel);

    // Convierte el objeto Entity de JPA de vuelta al Modelo de Dominio puro
    UserModel toUserModel(UserEntity userEntity);
}