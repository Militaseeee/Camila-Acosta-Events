package com.events_cav.events_venues.infrastructure.adapters.input.web.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventRequest {

    @NotBlank(message = "The event name is required")
    @Size(min = 3, max = 100, message = "The name must be between 3 and 100 characters long")
    private String name;

    @NotNull(message = "The date is mandatory")
    @Future(message = "The event must be on a future date")
    private LocalDate date;

    @NotNull(message = "Venue ID is required")
    private Long idVenue;

}