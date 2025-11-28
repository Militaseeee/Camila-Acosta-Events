package com.events_cav.events_venues.infrastructure.adapters.input.web.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = DateRangeValidator.class) // Esta parte es la LÓGICA
@Target({ElementType.TYPE}) // Se aplica a nivel de CLASE (en EventRequest)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidDateRange {

    // Mensaje de error personalizado (lo pondremos en messages.properties)
    String message() default "{event.dates.invalid_range}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}