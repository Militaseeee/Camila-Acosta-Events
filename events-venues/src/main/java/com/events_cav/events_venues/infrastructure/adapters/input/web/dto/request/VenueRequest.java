package com.events_cav.events_venues.infrastructure.adapters.input.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VenueRequest {

    @NotBlank(message = "The venue name is required")
    @Size(min = 3, max = 50, message = "The name must be between 3 and 50 characters long")
    private String name;

    @NotBlank(message = "Location is mandatory")
    private String location;
}