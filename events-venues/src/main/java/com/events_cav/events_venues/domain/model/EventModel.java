package com.events_cav.events_venues.domain.model;

import java.time.LocalDate;

public class EventModel {
    private Long id;
    private String name;
    private LocalDate date;

    private VenueModel venue;

    public EventModel() {
    }

    public EventModel(Long id, String name, LocalDate date, VenueModel venue) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.venue = venue;
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