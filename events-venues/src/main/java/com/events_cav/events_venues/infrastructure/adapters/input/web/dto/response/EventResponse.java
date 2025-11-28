package com.events_cav.events_venues.infrastructure.adapters.input.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventResponse {
    private Long id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private VenueResponse venue;
}