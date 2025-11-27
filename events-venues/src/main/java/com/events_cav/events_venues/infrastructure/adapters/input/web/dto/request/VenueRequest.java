package com.events_cav.events_venues.infrastructure.adapters.input.web.dto.request;

import com.events_cav.events_venues.infrastructure.adapters.input.web.validation.groups.ValidationGroups.OnCreate;
import com.events_cav.events_venues.infrastructure.adapters.input.web.validation.groups.ValidationGroups.OnUpdate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VenueRequest {

    @NotBlank(message = "{venue.name.notblank}", groups = {OnCreate.class, OnUpdate.class})
    @Size(min = 3, max = 50, message = "{venue.name.size}", groups = {OnCreate.class, OnUpdate.class})
    private String name;

    @NotBlank(message = "{venue.location.notblank}", groups = {OnCreate.class, OnUpdate.class})
    private String location;

    @NotBlank(message = "{venue.city.notblank}", groups = {OnCreate.class, OnUpdate.class})
    private String city;

    @NotNull(message = "{venue.capacity.notnull}", groups = {OnCreate.class, OnUpdate.class})
    @Min(value = 1, message = "{venue.capacity.min}", groups = {OnCreate.class, OnUpdate.class})
    private Integer capacity;
}