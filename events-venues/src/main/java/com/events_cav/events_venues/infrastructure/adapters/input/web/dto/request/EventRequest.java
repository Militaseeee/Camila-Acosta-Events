package com.events_cav.events_venues.infrastructure.adapters.input.web.dto.request;

import com.events_cav.events_venues.infrastructure.adapters.input.web.validation.groups.ValidationGroups;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.events_cav.events_venues.infrastructure.adapters.input.web.validation.ValidDateRange;
import com.events_cav.events_venues.infrastructure.adapters.input.web.validation.groups.ValidationGroups.OnCreate;
import com.events_cav.events_venues.infrastructure.adapters.input.web.validation.groups.ValidationGroups.OnUpdate;

import java.time.LocalDate;

@ValidDateRange(groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventRequest {

    @NotNull(message = "{event.id.notnull}", groups = {OnUpdate.class})
    private Long id;

    @NotBlank(message = "{event.name.notblank}", groups = {OnCreate.class, OnUpdate.class}) // Usando mensajes personalizados
    @Size(min = 3, max = 100, message = "{event.name.size}", groups = {OnCreate.class, OnUpdate.class})
    private String name;

    @NotNull(message = "{event.startDate.notnull}", groups = {OnCreate.class, OnUpdate.class})
    @FutureOrPresent(message = "{event.startDate.future}", groups = {OnCreate.class, OnUpdate.class})
    private LocalDate startDate;

    @NotNull(message = "{event.endDate.notnull}", groups = {OnCreate.class, OnUpdate.class})
    @FutureOrPresent(message = "{event.endDate.future}", groups = {OnCreate.class, OnUpdate.class})
    private LocalDate endDate;

    @NotNull(message = "{event.idvenue.notnull}", groups = {OnCreate.class, OnUpdate.class})
    private Long idVenue;

}