// src/main/java/com/events_cav/events_venues/domain/model/UserModel.java
package com.events_cav.events_venues.domain.model;

import java.util.Collection;
import java.util.List;

public class UserModel{

    private Long id;
    private String username;
    private String password;
    private Role role;

    public UserModel() {
    }

    public UserModel(Long id, String username, String password, Role role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}