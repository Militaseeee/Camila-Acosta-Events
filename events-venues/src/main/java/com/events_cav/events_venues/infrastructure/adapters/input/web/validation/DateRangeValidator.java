package com.events_cav.events_venues.infrastructure.adapters.input.web.validation;

import com.events_cav.events_venues.infrastructure.adapters.input.web.dto.request.EventRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, EventRequest> {

    @Override
    public boolean isValid(EventRequest request, ConstraintValidatorContext context) {
        // Si las fechas son nulas, dejamos que las validaciones @NotNull/FutureOrPresent las capturen
        if (request.getStartDate() == null || request.getEndDate() == null) {
            return true;
        }

        LocalDate startDate = request.getStartDate();
        LocalDate endDate = request.getEndDate();

        // No debe ser posterior (es decir, debe ser anterior o igual)
        boolean isValid = !startDate.isAfter(endDate);

        if (!isValid) {
            // Personalizo la respuesta del error
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("startDate")
                    .addConstraintViolation();
        }

        return isValid;
    }
}