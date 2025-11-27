package com.events_cav.events_venues.domain.model;

import java.time.LocalDate;

public class EventModel {
    private Long id;
    private String name;
//    private LocalDate date;

    private LocalDate startDate;
    private LocalDate endDate;

    private VenueModel venue;

    public EventModel() {
    }

    public EventModel(Long id, String name, LocalDate startDate, LocalDate endDate, VenueModel venue) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public VenueModel getVenue() {
        return venue;
    }

    public void setVenue(VenueModel venue) {
        this.venue = venue;
    }
}