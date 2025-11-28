package com.events_cav.events_venues.infrastructure.adapters.input.web.validation;

import com.events_cav.events_venues.infrastructure.adapters.input.web.dto.request.EventRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, EventRequest> {

    @Override
    public boolean isValid(EventRequest eventRequest, ConstraintValidatorContext context) {
        // Si las fechas son nulas, dejamos que las validaciones @NotNull/FutureOrPresent las capturen
        if (eventRequest == null || eventRequest.getStartDate() == null || eventRequest.getEndDate() == null) {
            return true;
        }

        LocalDate startDate = eventRequest.getStartDate();
        LocalDate endDate = eventRequest.getEndDate();

        // Lógica de Validación Cruzada
        boolean isValid = !startDate.isAfter(endDate);

        if (!isValid) {

            // Deshabilita el mensaje de error por defecto que se asocia a la clase (EventRequest)
            context.disableDefaultConstraintViolation();

            // Construiye la nueva restricción, al campo startDate y con el mensaje definido en la anotación que apunta a messages.properties
            context.buildConstraintViolationWithTemplate("{event.date.range.invalid}")
                    .addPropertyNode("startDate")
                    .addConstraintViolation();
        }

        return isValid;
    }
}