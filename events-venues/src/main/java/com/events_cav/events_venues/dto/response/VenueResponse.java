package com.events_cav.events_venues.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VenueResponse {
    private Long id;
    private String name;
    private String location;
}