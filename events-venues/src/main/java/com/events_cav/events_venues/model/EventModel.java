package com.events_cav.events_venues.model;

import java.time.LocalDate;
import java.util.Objects;

// POJO puro: NO lleva ninguna anotación de JPA, Spring ni Lombok.
public class EventModel {
    private Long id;
    private String name;
    private LocalDate date;

    // La referencia debe ser a otro objeto Model puro
    private VenueModel venue;

    // Constructor Vacío
    public EventModel() {
    }

    // Constructor Completo
    public EventModel(Long id, String name, LocalDate date, VenueModel venue) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.venue = venue;
    }

    // Getters y Setters

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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public VenueModel getVenue() {
        return venue;
    }

    public void setVenue(VenueModel venue) {
        this.venue = venue;
    }
}