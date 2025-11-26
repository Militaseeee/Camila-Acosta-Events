package com.events_cav.events_venues.domain.model;

import java.util.List;
import java.util.ArrayList;

public class VenueModel {
    private Long id;
    private String name;
    private String location;
    private String city;
    private Integer capacity;

    public VenueModel() {
    }

    public VenueModel(Long id, String name, String location, String city, Integer capacity) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.city = city;
        this.capacity = capacity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    // La lista de Eventos representada por el modelo de dominio ->esto es clave para que el dominio pueda acceder a los eventos
    private List<EventModel> events = new ArrayList<>();

    // Mrtodo de ayuda para la consistencia del modelo de dominio
    public void addEvent(EventModel event) {
        if (this.events == null) {
            this.events = new ArrayList<>();
        }
        this.events.add(event);
    }
}